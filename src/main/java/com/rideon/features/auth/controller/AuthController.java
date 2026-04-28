package com.rideon.features.auth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rideon.entity.RevokedToken;
import com.rideon.entity.User;
import com.rideon.exception.ResourceNotFoundException;
import com.rideon.features.auth.dto.LoginRequest;
import com.rideon.features.auth.dto.SignUpRequest;
import com.rideon.features.auth.service.AuthService;
import com.rideon.repository.RevokedTokenRepository;
import com.rideon.repository.UserRepository;
import com.rideon.security.JwtUtil;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/auth")
public class AuthController {

	 @Autowired
	 private JwtUtil jwtUtil;
	
     @Autowired
	 AuthService authService;
     
     @Autowired
     UserRepository userRepository;
     
     @Autowired
 	 private RevokedTokenRepository revokedTokenRepository;
	
	 @PostMapping("/signup")
	 public ResponseEntity<String> signup(@Valid @RequestBody SignUpRequest request) {
	     return ResponseEntity.status(HttpStatus.CREATED)
	                          .body(authService.signup(request));
	 }
	
	 @PostMapping("/login")
	 public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
		 return ResponseEntity.ok(authService.login(request));
	 }
	
	 @PostMapping("/logout")
	 public ResponseEntity<String> logout(@RequestHeader("Authorization") String token,
				@RequestBody Map<String, String> request) {
		token = token.substring(7);
		revokedTokenRepository.save(new RevokedToken(token));

		String refreshToken = request.get("refreshToken");
		if (refreshToken != null) {
			revokedTokenRepository.save(new RevokedToken(refreshToken));
		}

		return ResponseEntity.ok("Logged out successfully.");
	}
	 
	 @PostMapping("/refresh")
		public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
			String refreshToken = request.get("refreshToken");

			if (refreshToken == null || jwtUtil.isTokenExpired(refreshToken)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token.");
			}

			String mobile = jwtUtil.getMobileFromToken(refreshToken);
			
			User user = userRepository.findByMobile(mobile)
		            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

			return ResponseEntity.ok(Map.of("accessToken", jwtUtil.generateToken(mobile, user.getUserRole().name())));
		}
}

