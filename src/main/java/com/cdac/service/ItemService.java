package com.cdac.service;

import java.util.List;

import com.cdac.dto.request.CreateItemRequest;
import com.cdac.dto.response.ItemResponse;
import com.cdac.dto.response.ItemSummaryResponse;
import com.cdac.dto.response.MessageResponse;

public interface ItemService {

	ItemResponse createLostItem(CreateItemRequest request);

	ItemResponse createFoundItem(CreateItemRequest request);

    List<ItemSummaryResponse> getMyItems();

    ItemResponse getItemById(Long itemId);

    ItemResponse updateItem(
            Long itemId,
            CreateItemRequest request);

    MessageResponse withdrawItem(Long itemId);

}