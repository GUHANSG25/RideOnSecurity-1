package com.rideon.features.booking.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class BookingCreateRequest {

    @NotBlank
    private String bookingRef;      // ref from /initiate response

    @NotEmpty
    @Valid
    private List<PassengerDTO> passengers;

    // Getters & Setters
    public String getBookingRef() { return bookingRef; }
    public void setBookingRef(String bookingRef) { this.bookingRef = bookingRef; }
    public List<PassengerDTO> getPassengers() { return passengers; }
    public void setPassengers(List<PassengerDTO> passengers) { this.passengers = passengers; }
}