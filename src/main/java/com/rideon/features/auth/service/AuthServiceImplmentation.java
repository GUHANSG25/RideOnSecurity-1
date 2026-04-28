package com.rideon.features.auth.service;

import com.rideon.entity.Role;


import com.rideon.entity.User;
import com.rideon.exception.AuthenticationException;
import com.rideon.exception.ResourceNotFoundException;
import com.rideon.exception.ValidationException;
import com.rideon.features.auth.dto.LoginRequest;
import com.rideon.features.auth.dto.SignUpRequest;
import com.rideon.features.otp.service.OtpService;
import com.rideon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class AuthServiceImplmentation implements AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OtpService otpService;

    public String signup(SignUpRequest request) {
    	
    	if(request.getMobile().isEmpty() || request.getUserName().isEmpty()) {
    		throw new ValidationException("Please fill all the fields");
    	}

        if (userRepository.existsByMobile(request.getMobile())) {
            throw new AuthenticationException("Mobile number already registered");
        }

        otpService.holdSignupPending(request);

        otpService.generateOtp(request.getMobile());

        return "OTP sent to " + request.getMobile() + ". Please verify to complete signup.";
    }

    public String login(LoginRequest request) {

        User user = userRepository.findByMobile(request.getMobile())
                .orElseThrow(() -> new ResourceNotFoundException("Mobile number not registered. Please sign up."));

        if (Role.ROLE_OPERATOR.equals(user.getUserRole())) {
            if ("Pending".equalsIgnoreCase(user.getUserStatus())) {
                throw new AuthenticationException("Your operator request is still pending approval.");
            }
            if (!"Active".equalsIgnoreCase(user.getUserStatus())) {
                throw new AuthenticationException("Your account is inactive.");
            }
        }

        if (Role.ROLE_ADMIN.equals(user.getUserRole())) {
            if (!"Active".equalsIgnoreCase(user.getUserStatus())) {
                throw new AuthenticationException("Admin account is inactive.");
            }
        }

        otpService.generateOtp(request.getMobile());

        return "OTP sent to " + request.getMobile() + ". Please verify to login.";
    }

    public String logout() {
        return "Logged out successfully";
    }
}