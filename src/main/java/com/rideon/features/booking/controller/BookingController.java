package com.rideon.features.booking.controller;

import com.rideon.entity.User;
import com.rideon.features.booking.dto.*;
import com.rideon.features.booking.service.BookingService;
import com.rideon.features.profile.service.UserService;
import com.rideon.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired private BookingService bookingService;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserService userService;

    private int extractUserId(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization").substring(7);
        String mobile = jwtUtil.getMobileFromToken(token);
        Optional<User> user = userService.getUserProfile(mobile);
        return user.get().getUserId();
    }

    @PostMapping("/lock-seats")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<BookingInitiateResponse> lockSeats(
            @Valid @RequestBody BookingInitiateRequest request,
            HttpServletRequest httpRequest) {
        BookingInitiateResponse response = bookingService.lockSeats(extractUserId(httpRequest), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/release")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<Void> releaseSeats(
            @RequestBody List<Integer> seatIds) {
        bookingService.releaseLockedSeats(seatIds);
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/{bookingRef}")
//    @PreAuthorize("hasAuthority('ROLE_CUSTOMER') or hasAuthority('ROLE_OPERATOR')")
//    public ResponseEntity<BookingResponse> getBooking(@PathVariable String bookingRef) {
//        return ResponseEntity.ok(bookingService.getBookingByRef(bookingRef));
//    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<List<BookingResponse>> getMyBookings(HttpServletRequest httpRequest) {
        return ResponseEntity.ok(bookingService.getMyBookings(extractUserId(httpRequest)));
    }

    @PostMapping("/{bookingRef}/cancel")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable String bookingRef,
            HttpServletRequest httpRequest) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingRef, extractUserId(httpRequest)));
    }
    
    @GetMapping("/operator/{id}")
    @PreAuthorize("hasAuthority('ROLE_OPERATOR')")
    public ResponseEntity<List<BookingResponse>> getBookingsByOperatorId( @PathVariable int id,
            HttpServletRequest httpRequest){
    	return ResponseEntity.ok(bookingService.getBookingsByOperatorId(id));
    }
    
    @GetMapping("/cancel-list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<BookingResponse>> getCancelledBookings(){
    	return ResponseEntity.ok(bookingService.getCancelledBookings());
    }
}