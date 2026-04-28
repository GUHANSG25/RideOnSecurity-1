package com.rideon.features.auth.dto;

import com.rideon.entity.User;

public class LoginResponse {
	private String message;
    private User user;
    
    
    public LoginResponse() {}
    
	public LoginResponse(User user, String message) {
		super();
		this.message = message;
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
	
}