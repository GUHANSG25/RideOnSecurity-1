package com.rideon.features.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {
	@NotBlank(message = "Mobile number is required")
    @Size(min = 10, max = 10, message = "Mobile number should be exactly 10 numbers")
    private String mobile;

    public LoginRequest() {}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
