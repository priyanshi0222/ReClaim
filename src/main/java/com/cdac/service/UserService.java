package com.cdac.service;

import com.cdac.dto.request.ChangePasswordRequest;
import com.cdac.dto.request.UpdateProfileRequest;
import com.cdac.dto.response.UserProfileResponse;

public interface UserService {

    UserProfileResponse getCurrentUserProfile();

    UserProfileResponse updateProfile(UpdateProfileRequest request);

    void changePassword(ChangePasswordRequest request);

}