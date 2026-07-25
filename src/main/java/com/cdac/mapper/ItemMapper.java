package com.cdac.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.cdac.dto.response.ImageResponse;
import com.cdac.dto.response.ItemResponse;
import com.cdac.dto.response.ItemSummaryResponse;
import com.cdac.entity.Item;
import com.cdac.entity.ItemImage;
import com.cdac.entity.OwnershipQuestion;

public class ItemMapper {

    private ItemMapper() {
    }

    public static ItemResponse toItemResponse(
            Item item,
            List<ItemImage> images,
            List<OwnershipQuestion> questions) {

        return ItemResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .category(item.getCategory())
                .brand(item.getBrand())
                .color(item.getColor())
                .locationDescription(item.getLocationDescription())
                .itemDate(item.getItemDate())
                .itemType(item.getItemType())
                .status(item.getStatus())
                .createdAt(item.getCreatedAt())
                .images(toImageResponse(images))
                .ownershipQuestions(toOwnershipQuestions(questions))
                .build();
    }

    public static ItemSummaryResponse toSummaryResponse(
            Item item,
            List<ItemImage> images) {

        String thumbnail = null;

        if (!images.isEmpty()) {
            thumbnail = images.get(0).getImageUrl();
        }

        return ItemSummaryResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .category(item.getCategory())
                .color(item.getColor())
                .locationDescription(item.getLocationDescription())
                .itemDate(item.getItemDate())
                .itemType(item.getItemType())
                .status(item.getStatus())
                .thumbnailImageUrl(thumbnail)
                .build();
    }

    private static List<ImageResponse> toImageResponse(
            List<ItemImage> images) {

        if (images == null) {
            return Collections.emptyList();
        }

        return images.stream()
                .map(image -> ImageResponse.builder()
                        .id(image.getId())
                        .imageUrl(image.getImageUrl())
                        .displayOrder(image.getDisplayOrder())
                        .build())
                .collect(Collectors.toList());
    }

    private static List<String> toOwnershipQuestions(
            List<OwnershipQuestion> questions) {

        if (questions == null) {
            return Collections.emptyList();
        }

        return questions.stream()
                .map(OwnershipQuestion::getQuestionText)
                .collect(Collectors.toList());
    }
}