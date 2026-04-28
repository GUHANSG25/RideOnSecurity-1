package com.rideon.features.payment.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.rideon.entity.User;
import com.rideon.features.booking.dto.BookingResponse;
import com.rideon.features.payment.dto.RazorpayOrderRequest;
import com.rideon.features.payment.dto.RazorpayOrderResponse;
import com.rideon.features.payment.dto.RazorpayVerifyRequest;
import com.rideon.features.payment.service.PaymentService;
import com.rideon.features.profile.service.UserService;
import com.rideon.security.JwtUtil;

import java.util.Optional;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired 
    private PaymentService paymentService;
    @Autowired 
    private JwtUtil jwtUtil;
    @Autowired 
    private UserService userService;

    private User extractUser(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization").substring(7);
        String mobile = jwtUtil.getMobileFromToken(token);
        Optional<User> user = userService.getUserProfile(mobile);
        return user.orElseThrow(() ->
            new com.rideon.exception.ResourceNotFoundException("Authenticated user not found"));
    }

    @PostMapping("/order")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<RazorpayOrderResponse> createOrder(
            @Valid @RequestBody RazorpayOrderRequest request) {
        return ResponseEntity.ok(paymentService.createOrder(request));
    }

    @PostMapping("/verify")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<BookingResponse> verifyPayment(
            @Valid @RequestBody RazorpayVerifyRequest request,
            HttpServletRequest httpRequest) {
        User user = extractUser(httpRequest);
        return ResponseEntity.ok(paymentService.verifyAndConfirm(request, user));
    }
}