package com.rideon.features.profile.service;

import java.util.Optional;

import com.rideon.entity.User;

public interface UserService {
	Optional<User> getUserProfile(String mobile);
	
	User updateProfile(String mobile,User user);
}
