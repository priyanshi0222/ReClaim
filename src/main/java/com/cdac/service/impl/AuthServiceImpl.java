package com.cdac.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.cdac.dto.request.LoginRequest;
import com.cdac.dto.request.RegisterRequest;
import com.cdac.dto.response.AuthResponse;
import com.cdac.entity.User;
import com.cdac.enums.AccountStatus;
import com.cdac.enums.UserRole;
import com.cdac.exception.DuplicateResourceException;
import com.cdac.service.AuthService;
import com.cdac.repository.UserRepository;
import com.cdac.security.CustomUserDetails;
import com.cdac.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService {
	   
	    //require for registration
	    private final UserRepository userRepository;
	    private final PasswordEncoder passwordEncoder;
	    private final JwtService jwtService;
	    
	    //required for login
	    private final AuthenticationManager authenticationManager;
	    
	    
	@Override
	@Transactional
	public AuthResponse register(RegisterRequest request) {

	    // Check if email already exists
	    if (userRepository.existsByEmail(request.getEmail())) {
	    	throw new DuplicateResourceException(
	    		    "An account with this email already exists.");
	    }

	    // Create User entity
	    User user = User.builder()
	            .fullName(request.getFullName())
	            .email(request.getEmail())
	            .password(passwordEncoder.encode(request.getPassword()))
	            .phoneNumber(request.getPhoneNumber())
	            .role(UserRole.USER)
	            .accountStatus(AccountStatus.ACTIVE)
	            .build();

	    // Save user
	    User savedUser = userRepository.save(user);

	    // Generate JWT
	    String jwtToken = jwtService.generateToken( new CustomUserDetails(savedUser));

	    // Return response
	    return buildAuthResponse(savedUser, jwtToken);
	}

	@Override
	public AuthResponse login(LoginRequest request) {

	    // Authenticate user
	    Authentication authentication = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(
	                    request.getEmail(),
	                    request.getPassword()
	            )
	    );

	    // Get authenticated user
	    CustomUserDetails userDetails =
	            (CustomUserDetails) authentication.getPrincipal();

	    User user = userDetails.getUser();

	    // Generate JWT
	    String jwtToken = jwtService.generateToken(userDetails);

	    // Return response
	    return buildAuthResponse(user,jwtToken);
	}
	/**
	 * Builds authentication response after successful registration/login to follow DRY principle
	 */
	private AuthResponse buildAuthResponse(User user, String jwtToken) {

	    return AuthResponse.builder()
	            .token(jwtToken)
	            .userId(user.getId())
	            .fullName(user.getFullName())
	            .email(user.getEmail())
	            .role(user.getRole())
	            .build();
	}
}
