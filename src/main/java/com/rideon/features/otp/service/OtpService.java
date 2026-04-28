package com.rideon.features.otp.service;

import com.rideon.features.auth.dto.SignUpRequest;
import com.rideon.features.otp.dto.OtpVerifyResponse;

public interface OtpService {
	public void holdSignupPending(SignUpRequest request);
	public String generateOtp(String phone);
	public OtpVerifyResponse verifyOtp(String phone,String otp, String purpose);
}
