package com.rideon.features.profile.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rideon.entity.User;
import com.rideon.exception.ResourceNotFoundException;
import com.rideon.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public Optional<User> getUserProfile(String mobile) {
		return userRepository.findByMobile(mobile);
	}
	
	public User updateProfile(String mobile,User request) {
		User existingUser = userRepository.findByMobile(mobile).orElseThrow(() -> new ResourceNotFoundException("User details not found"));
		
		if(request.getUserName() != null) {
			existingUser.setUserName(request.getUserName());
		}
		if(request.getEmail() != null) {
			existingUser.setEmail(request.getEmail());
		}
		
		return userRepository.save(existingUser);
	}
}
