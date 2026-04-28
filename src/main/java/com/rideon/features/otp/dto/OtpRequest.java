package com.rideon.features.otp.dto;

public class OtpRequest {
    private String mobile;
    
    public OtpRequest() {}

	public OtpRequest(String mobile) {
		super();
		this.mobile = mobile;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
    
    
}