package com.cdac.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cdac.dto.request.CreateItemRequest;
import com.cdac.dto.response.ItemResponse;
import com.cdac.dto.response.ItemSummaryResponse;
import com.cdac.dto.response.MessageResponse;

public interface ItemService {

    ItemResponse createLostItem(
            CreateItemRequest request,
            List<MultipartFile> images);

    ItemResponse createFoundItem(
            CreateItemRequest request,
            List<MultipartFile> images);

    List<ItemSummaryResponse> getMyItems();

    ItemResponse getItemById(Long itemId);

    ItemResponse updateItem(
            Long itemId,
            CreateItemRequest request,
            List<MultipartFile> images);

    MessageResponse withdrawItem(Long itemId);

}