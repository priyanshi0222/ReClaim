package com.cdac.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cdac.dto.request.CreateItemRequest;
import com.cdac.dto.request.OwnershipQuestionRequest;
import com.cdac.dto.response.ItemResponse;
import com.cdac.dto.response.ItemSummaryResponse;
import com.cdac.dto.response.MessageResponse;
import com.cdac.entity.Item;
import com.cdac.entity.ItemImage;
import com.cdac.entity.OwnershipQuestion;
import com.cdac.entity.User;
import com.cdac.enums.ItemStatus;
import com.cdac.enums.ItemType;
import com.cdac.exception.InvalidOperationException;
import com.cdac.exception.ResourceNotFoundException;
import com.cdac.mapper.ItemMapper;
import com.cdac.repository.ItemImageRepository;
import com.cdac.repository.ItemRepository;
import com.cdac.repository.OwnershipQuestionRepository;
import com.cdac.service.CurrentUserService;
import com.cdac.service.FileStorageService;
import com.cdac.service.ItemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;
    private final OwnershipQuestionRepository ownershipQuestionRepository;
    private final CurrentUserService currentUserService;
    private final FileStorageService fileStorageService;

    @Override
    public ItemResponse createLostItem(CreateItemRequest request,
                                       List<MultipartFile> images) {

        User currentUser = currentUserService.getCurrentUser();

        Item item = Item.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .brand(request.getBrand())
                .color(request.getColor())
                .locationDescription(request.getLocationDescription())
                .itemDate(request.getItemDate())
                .itemType(ItemType.LOST)
                .status(ItemStatus.OPEN)
                .reportedBy(currentUser)
                .build();

        Item savedItem = itemRepository.save(item);

        saveImages(savedItem, images);

        return ItemMapper.toItemResponse(
                savedItem,
                itemImageRepository.findByItemOrderByDisplayOrderAsc(savedItem),
                new ArrayList<>()
        );
    }

    @Override
    public ItemResponse createFoundItem(CreateItemRequest request,
                                        List<MultipartFile> images) {

        if (request.getOwnershipQuestions() == null
                || request.getOwnershipQuestions().size() < 3) {

            throw new InvalidOperationException(
                    "At least 3 ownership questions are required.");
        }

        User currentUser = currentUserService.getCurrentUser();

        Item item = Item.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .brand(request.getBrand())
                .color(request.getColor())
                .locationDescription(request.getLocationDescription())
                .itemDate(request.getItemDate())
                .itemType(ItemType.FOUND)
                .status(ItemStatus.OPEN)
                .reportedBy(currentUser)
                .build();

        Item savedItem = itemRepository.save(item);

        saveImages(savedItem, images);

        int displayOrder = 1;

        for (OwnershipQuestionRequest questionRequest
                : request.getOwnershipQuestions()) {

            OwnershipQuestion question = OwnershipQuestion.builder()
                    .item(savedItem)
                    .questionText(questionRequest.getQuestionText())
                    .displayOrder(displayOrder++)
                    .build();

            ownershipQuestionRepository.save(question);
        }

        return ItemMapper.toItemResponse(
                savedItem,	
                itemImageRepository.findByItemOrderByDisplayOrderAsc(savedItem),
                ownershipQuestionRepository.findByItemOrderByDisplayOrderAsc(savedItem)
        );
    }

    private void saveImages(Item item,
                            List<MultipartFile> images) {

        if (images == null || images.isEmpty()) {
            return;
        }

        int displayOrder = 1;

        for (MultipartFile file : images) {

            String fileName = fileStorageService.storeFile(file);

            ItemImage image = ItemImage.builder()
                    .item(item)
                    .imageUrl(fileName)
                    .displayOrder(displayOrder++)
                    .build();

            itemImageRepository.save(image);
        }
    }
    @Override
    @Transactional(readOnly = true)
    public List<ItemSummaryResponse> getMyItems() {

        User currentUser = currentUserService.getCurrentUser();

        List<Item> items =
        		itemRepository.findByReportedByOrderByCreatedAtDesc(currentUser);

        List<ItemSummaryResponse> response = new ArrayList<>();

        for (Item item : items) {

            List<ItemImage> images =
                    itemImageRepository.findByItemOrderByDisplayOrderAsc(item);

            response.add(
                    ItemMapper.toSummaryResponse(item, images)
            );
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponse getItemById(Long itemId) {

        User currentUser = currentUserService.getCurrentUser();

        Item item = itemRepository
        		 .findByIdAndReportedBy(itemId, currentUser)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Item not found."));

        List<ItemImage> images =
                itemImageRepository.findByItemOrderByDisplayOrderAsc(item);

        List<OwnershipQuestion> questions = new ArrayList<>();

        if (item.getItemType() == ItemType.FOUND) {
            questions = ownershipQuestionRepository
                    .findByItemOrderByDisplayOrderAsc(item);
        }

        return ItemMapper.toItemResponse(
                item,
                images,
                questions
        );
    }

    @Override
    public ItemResponse updateItem(Long itemId,
                                   CreateItemRequest request,
                                   List<MultipartFile> images) {

        User currentUser = currentUserService.getCurrentUser();

        Item item = itemRepository
                .findByIdAndReportedBy(itemId, currentUser)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Item not found."));

        if (item.getStatus() != ItemStatus.OPEN) {
            throw new InvalidOperationException(
                    "Only open items can be updated.");
        }

        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setCategory(request.getCategory());
        item.setBrand(request.getBrand());
        item.setColor(request.getColor());
        item.setLocationDescription(request.getLocationDescription());
        item.setItemDate(request.getItemDate());

        Item updatedItem = itemRepository.save(item);

        // Delete existing images
        List<ItemImage> oldImages =
                itemImageRepository.findByItemOrderByDisplayOrderAsc(updatedItem);

        for (ItemImage image : oldImages) {
            fileStorageService.deleteFile(image.getImageUrl());
        }

        itemImageRepository.deleteByItem(updatedItem);

        saveImages(updatedItem, images);

        // Update ownership questions only for found items
        if (updatedItem.getItemType() == ItemType.FOUND) {

            ownershipQuestionRepository.deleteByItem(updatedItem);

            if (request.getOwnershipQuestions() != null) {

                int displayOrder = 1;

                for (OwnershipQuestionRequest questionRequest
                        : request.getOwnershipQuestions()) {

                    OwnershipQuestion question = OwnershipQuestion.builder()
                            .item(updatedItem)
                            .questionText(questionRequest.getQuestionText())
                            .displayOrder(displayOrder++)
                            .build();

                    ownershipQuestionRepository.save(question);
                }
            }
        }
        List<OwnershipQuestion> questions = new ArrayList<>();

        if (updatedItem.getItemType() == ItemType.FOUND) {
            questions = ownershipQuestionRepository
                    .findByItemOrderByDisplayOrderAsc(updatedItem);
        }

        return ItemMapper.toItemResponse(
                updatedItem,
                itemImageRepository.findByItemOrderByDisplayOrderAsc(updatedItem),
                questions
        );
}  
    @Override
    public MessageResponse withdrawItem(Long itemId) {

    User currentUser = currentUserService.getCurrentUser();

    Item item = itemRepository
            .findByIdAndReportedBy(itemId, currentUser)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Item not found."));

    if (item.getStatus() != ItemStatus.OPEN) {
        throw new InvalidOperationException(
                "Only open items can be withdrawn.");
    }

    item.setStatus(ItemStatus.CANCELLED);

    itemRepository.save(item);

    return MessageResponse.builder()
            .message("Item withdrawn successfully.")
            .build();
}

}
