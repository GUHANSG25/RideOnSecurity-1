package com.rideon.features.otp.controller;

import com.rideon.features.otp.dto.OtpRequest;
import com.rideon.features.otp.dto.OtpVerifyRequest;
import com.rideon.features.otp.dto.OtpVerifyResponse;
import com.rideon.features.otp.service.OtpServiceImplementation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp")
public class OtpController {

    private final OtpServiceImplementation otpService;

    public OtpController(OtpServiceImplementation otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/resend")
    public ResponseEntity<String> resendOtp(@RequestBody OtpRequest request) {
        String res = otpService.generateOtp(request.getMobile());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
    }

    @PostMapping("/verify")
    public ResponseEntity<OtpVerifyResponse> verifyOtp(@RequestBody OtpVerifyRequest request) {
        OtpVerifyResponse response = otpService.verifyOtp(
                request.getMobile(),
                request.getOtp(),
                request.getPurpose()
        );

        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}