package com.cdac.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.dto.request.ChangePasswordRequest;
import com.cdac.dto.request.UpdateProfileRequest;
import com.cdac.dto.response.UserProfileResponse;
import com.cdac.entity.User;
import com.cdac.exception.InvalidOperationException;
import com.cdac.exception.UnauthorizedException;
import com.cdac.repository.UserRepository;
import com.cdac.service.CurrentUserService;
import com.cdac.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final CurrentUserService currentUserService;

    private final PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public UserProfileResponse updateProfile(UpdateProfileRequest request) {

        User user = currentUserService.getCurrentUser();

        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());

        User updatedUser = userRepository.save(user);

        return mapToUserProfileResponse(updatedUser);
    }

    private UserProfileResponse mapToUserProfileResponse(User user) {

        return UserProfileResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .accountStatus(user.getAccountStatus())
                .build();
    }
    
    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {

        User user = currentUserService.getCurrentUser();

        // Verify current password
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            throw new UnauthorizedException("Current password is incorrect.");
        }

        // Prevent using the same password again
        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword())) {

            throw new InvalidOperationException(
                    "New password must be different from the current password.");
        }

        // Encode and save new password
        user.setPassword(
                passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }
    @Override
    public UserProfileResponse getCurrentUserProfile() {

        User user = currentUserService.getCurrentUser();

        return mapToUserProfileResponse(user);
    }
}