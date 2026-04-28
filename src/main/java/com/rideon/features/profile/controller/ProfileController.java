package com.rideon.features.profile.controller;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rideon.entity.User;
import com.rideon.exception.ResourceNotFoundException;
import com.rideon.features.profile.service.UserService;
import com.rideon.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class ProfileController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@GetMapping("/profile")
	public ResponseEntity<Optional<User>> getUserProfile(HttpServletRequest httpRequest) {
		String authHeader = httpRequest.getHeader("Authorization");
        String token = authHeader.substring(7);
        String mobile = jwtUtil.getMobileFromToken(token);
        
		Optional<User> user = userService.getUserProfile(mobile);
		return new ResponseEntity<>(user,HttpStatus.OK);
		
	}
	
	@PatchMapping("/profile/update")
	public ResponseEntity<User> updateProfile(HttpServletRequest httpRequest,@RequestBody User request) {
		
		String authHeader = httpRequest.getHeader("Authorization");
        String token = authHeader.substring(7);
        String mobile = jwtUtil.getMobileFromToken(token);
        
		Optional<User> user = userService.getUserProfile(mobile);
		
		if(user.isEmpty()) {
			throw new ResourceNotFoundException("User Not found");
		}
		
		User updatedUser = userService.updateProfile(mobile,request);
		
		return new ResponseEntity<>(updatedUser,HttpStatus.OK);
		
	} 
}
