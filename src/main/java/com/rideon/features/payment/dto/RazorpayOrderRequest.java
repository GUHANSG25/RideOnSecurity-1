package com.rideon.features.payment.dto;

import jakarta.validation.constraints.Positive;

public class RazorpayOrderRequest {
    // FIX: bookingRef removed — no booking row exists yet
    @Positive
    private Double totalFare;  // from lockSeats response, stored in Redux

    public Double getTotalFare() { return totalFare; }
    public void setTotalFare(Double totalFare) { this.totalFare = totalFare; }
}