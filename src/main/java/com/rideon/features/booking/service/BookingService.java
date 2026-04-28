package com.rideon.features.booking.service;

import com.rideon.features.booking.dto.*;
import java.util.List;

public interface BookingService {
    BookingInitiateResponse lockSeats(int userId, BookingInitiateRequest request);
    void releaseLockedSeats(List<Integer> seatIds);   // NEW — replaces old releaseSeats
    BookingResponse getBookingByRef(String bookingRef);
    List<BookingResponse> getMyBookings(int userId);
    BookingResponse cancelBooking(String bookingRef, int userId);
    List<BookingResponse> getBookingsByOperatorId(int operatorId);
    List<BookingResponse> getCancelledBookings();
    // confirmBooking() DELETED — PaymentService handles atomic save now
}