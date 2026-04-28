package com.rideon.features.otp.dto;

public class OtpVerifyRequest {
    private String mobile;
    private String otp;
    private String purpose; // "LOGIN" or "SIGNUP"

    public OtpVerifyRequest() {}

    public OtpVerifyRequest(String mobile, String otp, String purpose) {
        this.mobile = mobile;
        this.otp = otp;
        this.purpose = purpose;
    }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
}