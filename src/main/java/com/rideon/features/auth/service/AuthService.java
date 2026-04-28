package com.rideon.features.auth.service;


import com.rideon.features.auth.dto.LoginRequest;
import com.rideon.features.auth.dto.SignUpRequest;

public interface AuthService {
	String signup(SignUpRequest request);
    String login(LoginRequest request);
    String logout();
}
