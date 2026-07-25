package com.cdac.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

//    @PostMapping(
//            value = "/lost",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ItemResponse> createLostItem(
//            @Valid @RequestPart("item") CreateItemRequest request,
//            @RequestPart("images") List<MultipartFile> images) {
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(itemService.createLostItem(request, images));
//    }
    @PostMapping(
    	    value = "/lost",
    	    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    	)
    	public ResponseEntity<ItemResponse> createLostItem(
    	        @io.swagger.v3.oas.annotations.parameters.RequestBody(
    	                content = @io.swagger.v3.oas.annotations.media.Content(
    	                        mediaType = MediaType.APPLICATION_JSON_VALUE
    	                )
    	        )
    	        @Valid
    	        @RequestPart("item") CreateItemRequest request,

    	        @RequestPart("images") List<MultipartFile> images) {

    	    return ResponseEntity.status(HttpStatus.CREATED)
    	            .body(itemService.createLostItem(request, images));
    	}

    @PostMapping(
            value = "/found",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemResponse> createFoundItem(
            @Valid @RequestPart("item") CreateItemRequest request,
            @RequestPart("images") List<MultipartFile> images) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(itemService.createFoundItem(request, images));
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

    @PutMapping(
            value = "/{itemId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemResponse> updateItem(
            @PathVariable Long itemId,
            @Valid @RequestPart("item") CreateItemRequest request,
            @RequestPart("images") List<MultipartFile> images) {

        return ResponseEntity.ok(
                itemService.updateItem(itemId, request, images));
    }

    @PatchMapping("/{itemId}/withdraw")
    public ResponseEntity<MessageResponse> withdrawItem(
            @PathVariable Long itemId) {

        return ResponseEntity.ok(
                itemService.withdrawItem(itemId));
    }
}