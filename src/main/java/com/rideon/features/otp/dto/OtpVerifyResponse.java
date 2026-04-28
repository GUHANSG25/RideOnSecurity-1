package com.rideon.features.otp.dto;


public class OtpVerifyResponse {
    private boolean success;
    private String message;
    private String token;
    private String refreshToken;

    public OtpVerifyResponse() {}

    public OtpVerifyResponse(boolean success, String message, String token,
                              String refreshToken) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    // Getters & Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    

    public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}