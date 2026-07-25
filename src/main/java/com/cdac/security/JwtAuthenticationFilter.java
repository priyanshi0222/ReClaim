package com.cdac.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
    		
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

//    	System.out.println("JwtAuthenticationFilter is running...");
//        final String authHeader = request.getHeader("Authorization");
//
//        // No Authorization header
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        try {
//
//            final String jwt = authHeader.substring(7);
//
//            final String email = jwtService.extractUsername(jwt);
//
//            if (email != null &&
//                    SecurityContextHolder.getContext().getAuthentication() == null) {
//
//                UserDetails userDetails =
//                        customUserDetailsService.loadUserByUsername(email);
//
//                if (jwtService.isTokenValid(jwt, userDetails)) {
//
//                    UsernamePasswordAuthenticationToken authentication =
//                            new UsernamePasswordAuthenticationToken(
//                                    userDetails,
//                                    null,
//                                    userDetails.getAuthorities());
//
//                    authentication.setDetails(
//                            new WebAuthenticationDetailsSource()
//                                    .buildDetails(request));
//
//                    SecurityContextHolder
//                            .getContext()
//                            .setAuthentication(authentication);
//                }
//            }
//
//        } 
////        catch (JwtException | IllegalArgumentException ex) {
////
////            SecurityContextHolder.clearContext();
////        }
//        catch (Exception ex) {
//        ex.printStackTrace();   // <-- add this
//        SecurityContextHolder.clearContext();
//    }
//
//        filterChain.doFilter(request, response);
    	
    
    	System.out.println("========== JWT FILTER ==========");

    	final String authHeader = request.getHeader("Authorization");
    	System.out.println("Header : " + authHeader);

    	if (authHeader == null || !authHeader.startsWith("Bearer ")) {
    	    System.out.println("No Bearer Token");
    	    filterChain.doFilter(request, response);
    	    return;
    	}

    	try {

    	    String jwt = authHeader.substring(7);
    	    System.out.println("JWT : " + jwt);

    	    String email = jwtService.extractUsername(jwt);
    	    System.out.println("EMAIL : " + email);

    	    UserDetails userDetails =
    	            customUserDetailsService.loadUserByUsername(email);

    	    System.out.println("Loaded User : " + userDetails.getUsername());

    	    boolean valid = jwtService.isTokenValid(jwt, userDetails);

    	    System.out.println("VALID : " + valid);

    	    if(valid){

    	        UsernamePasswordAuthenticationToken authentication =
    	                new UsernamePasswordAuthenticationToken(
    	                        userDetails,
    	                        null,
    	                        userDetails.getAuthorities());

    	        SecurityContextHolder.getContext().setAuthentication(authentication);

    	        System.out.println("AUTHENTICATION SET");
    	    }

    	}catch(Exception e){
    	    e.printStackTrace();
    	}

    	filterChain.doFilter(request,response);}
}