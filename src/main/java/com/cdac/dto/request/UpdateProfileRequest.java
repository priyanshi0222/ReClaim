package com.cdac.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class UpdateProfileRequest {

    @NotBlank(message = "Full name is required.")
    @Size(max = 100, message = "Full name cannot exceed 100 characters.")
    private String fullName;

    @Pattern(
        regexp = "^[6-9]\\d{9}$",
        message = "Phone number must be a valid 10-digit Indian mobile number."
    )
    private String phoneNumber;

}