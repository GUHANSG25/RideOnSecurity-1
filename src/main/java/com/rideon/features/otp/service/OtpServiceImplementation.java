package com.rideon.features.otp.service;


import com.rideon.entity.Role;
import com.rideon.entity.User;
import com.rideon.features.auth.dto.SignUpRequest;
import com.rideon.features.otp.dto.OtpVerifyResponse;
import com.rideon.repository.UserRepository;
import com.rideon.security.JwtUtil;
import com.twilio.exception.ApiException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class OtpServiceImplementation implements OtpService{

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;
    
    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();
    private final Map<String, SignUpRequest> pendingSignups = new ConcurrentHashMap<>();

    private final UserRepository userRepository;

    public OtpServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void holdSignupPending(SignUpRequest request) {
        pendingSignups.put(request.getMobile(), request);
    }

    public String generateOtp(String phone) {
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        otpStore.put(phone, new OtpData(otp, LocalDateTime.now().plusMinutes(3)));
        
        if(phone.equals("9360516462")) {
        	try {
        		String formattedPhone = phone.startsWith("+") ? phone : "+91" + phone;
                Message.creator(
                    new PhoneNumber(formattedPhone),  
                    new PhoneNumber(twilioPhoneNumber), 
                    "Your OTP is: " + otp + ". Valid for 3 minutes. Do not share it with anyone."
                ).create();
            } catch (ApiException e) {
                throw new RuntimeException("Failed to send OTP via SMS: " + e.getMessage());
            }
        }else {
        	System.out.println("OTP for " + phone + ": " + otp);
        }
        return "OTP sent successfully";
    }

    public OtpVerifyResponse verifyOtp(String phone, String otp, String purpose) {

        OtpData data = otpStore.get(phone);

        if (data == null) {
            return new OtpVerifyResponse(false, "OTP not found. Please request a new one.",
                    null, null);
        }

        if (data.getExpiry().isBefore(LocalDateTime.now())) {
            otpStore.remove(phone);
            pendingSignups.remove(phone);
            return new OtpVerifyResponse(false, "OTP has expired. Please request a new one.",
                    null, null);
        }

        if (!data.getOtp().equals(otp)) {
            return new OtpVerifyResponse(false, "Invalid OTP. Please try again.",
                    null, null);
        }

        otpStore.remove(phone);

        if ("SIGNUP".equalsIgnoreCase(purpose)) {

            SignUpRequest pending = pendingSignups.remove(phone);

            if (pending == null) {
                return new OtpVerifyResponse(false, "Signup session expired. Please sign up again.",
                        null, null);
            }

            User user = new User();
            user.setUserName(pending.getUserName());
            user.setMobile(pending.getMobile());
            user.setUserRole(pending.getUserRole());
            user.setUserCreatedDate(LocalDate.now());
            user.setUserStatus(Role.ROLE_CUSTOMER.equals(pending.getUserRole()) ? "Active" : "Pending");
            userRepository.save(user);

            return new OtpVerifyResponse(true, "Phone verified. Account created successfully.",
                    null, null);
        }

        if ("LOGIN".equalsIgnoreCase(purpose)) {

            User user = userRepository.findByMobile(phone).orElse(null);

            if (user == null) {
                return new OtpVerifyResponse(false, "No account found. Please sign up.",
                        null, null);
            }

            String token = jwtUtil.generateToken(user.getMobile(), user.getUserRole().name());
            String refreshToken = jwtUtil.generateRefreshToken(user.getMobile());

            return new OtpVerifyResponse(true, "Login successful. Welcome " + user.getUserName(),
                    token, refreshToken);
        }

        return new OtpVerifyResponse(false, "Unknown purpose. Use LOGIN or SIGNUP.",
                null, null);
    }

    private static class OtpData {
        private final String otp;
        private final LocalDateTime expiry;

        public OtpData(String otp, LocalDateTime expiry) {
            this.otp = otp;
            this.expiry = expiry;
        }

        public String getOtp() { return otp; }
        public LocalDateTime getExpiry() { return expiry; }
    }
}