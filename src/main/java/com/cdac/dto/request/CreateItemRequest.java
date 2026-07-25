package com.cdac.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateItemRequest {

    @NotBlank(message = "Title is required.")
    @Size(max = 150, message = "Title cannot exceed 150 characters.")
    private String title;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters.")
    private String description;

    @NotBlank(message = "Category is required.")
    private String category;

    @Size(max = 100, message = "Brand cannot exceed 100 characters.")
    private String brand;

    @Size(max = 50, message = "Color cannot exceed 50 characters.")
    private String color;

    @NotBlank(message = "Location is required.")
    @Size(max = 255, message = "Location cannot exceed 255 characters.")
    private String locationDescription;

    @NotNull(message = "Item date is required.")
    @PastOrPresent(message = "Item date cannot be in the future.")
    private LocalDate itemDate;
    
    @Size(max = 5, message = "Maximum 5 ownership questions are allowed.")
    private List<OwnershipQuestionRequest> ownershipQuestions;
    
    @Size(max = 5, message = "Maximum 5 images are allowed.")
    private List<String> imageUrls;
}