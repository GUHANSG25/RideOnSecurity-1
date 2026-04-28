package com.rideon.features.payment.service;

import com.rideon.entity.User;
import com.rideon.features.booking.dto.BookingResponse;
import com.rideon.features.payment.dto.RazorpayOrderRequest;
import com.rideon.features.payment.dto.RazorpayOrderResponse;
import com.rideon.features.payment.dto.RazorpayVerifyRequest;

public interface PaymentService {
	RazorpayOrderResponse createOrder(RazorpayOrderRequest request);
	BookingResponse verifyAndConfirm(RazorpayVerifyRequest request, User user);
}