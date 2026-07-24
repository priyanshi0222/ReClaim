package com.cdac.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cdac.entity.User;
import com.cdac.exception.ResourceNotFoundException;
import com.cdac.exception.UnauthorizedException;
import com.cdac.repository.UserRepository;
import com.cdac.security.CustomUserDetails;
import com.cdac.service.CurrentUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurrentUserServiceImpl implements CurrentUserService {

	 private final UserRepository userRepository;

    @Override
    public User getCurrentUser() {

    	Authentication authentication = SecurityContextHolder
    	        .getContext()
    	        .getAuthentication();

    	if (authentication == null || !authentication.isAuthenticated()
    	        || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
    	    throw new UnauthorizedException("User is not authenticated.");
    	}

    	 String email = userDetails.getUsername();

         return userRepository.findByEmail(email)
                 .orElseThrow(() ->
                         new ResourceNotFoundException("Authenticated user no longer exists."));
    }

}