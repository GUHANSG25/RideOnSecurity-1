package com.rideon.features.auth.dto;

import com.rideon.entity.Role;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SignUpRequest {
    @NotBlank(message = "User name is required")
    @Size(min = 3, max = 20, message = "User name must be between 3 and 20 characters")
    private String userName;

    @NotBlank(message = "Mobile number is required")
    @Size(min = 10, max = 10, message = "Mobile number should be exactly 10 numbers")
    private String mobile;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role is required")
    private Role userRole;	
	public SignUpRequest() {}

	public SignUpRequest(String userName, String mobile, Role userRole) {
		super();
		this.userName = userName;
		this.mobile = mobile;
		this.userRole = userRole;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Role getUserRole() {
		return userRole;
	}

	public void setUserRole(Role userRole) {
		this.userRole = userRole;
	}
	
	
}
