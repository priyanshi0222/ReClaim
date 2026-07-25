package com.cdac.controller;

import java.util.List;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.cdac.dto.request.CreateItemRequest;
import com.cdac.dto.response.ItemResponse;
import com.cdac.dto.response.ItemSummaryResponse;
import com.cdac.dto.response.MessageResponse;
import com.cdac.service.ItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/lost")
    public ResponseEntity<ItemResponse> createLostItem(
            @Valid @RequestBody CreateItemRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(itemService.createLostItem(request));
    }
    

    @PostMapping("/found")
    public ResponseEntity<ItemResponse> createFoundItem(
            @Valid @RequestBody CreateItemRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(itemService.createFoundItem(request));
    }
    

    @GetMapping("/my-items")
    public ResponseEntity<List<ItemSummaryResponse>> getMyItems() {

        return ResponseEntity.ok(itemService.getMyItems());
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponse> getItemById(
            @PathVariable Long itemId) {

        return ResponseEntity.ok(itemService.getItemById(itemId));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ItemResponse> updateItem(
            @PathVariable Long itemId,
            @Valid @RequestBody CreateItemRequest request) {

        return ResponseEntity.ok(
                itemService.updateItem(itemId, request));
    }

    @PatchMapping("/{itemId}/withdraw")
    public ResponseEntity<MessageResponse> withdrawItem(
            @PathVariable Long itemId) {

        return ResponseEntity.ok(
                itemService.withdrawItem(itemId));
    }
}