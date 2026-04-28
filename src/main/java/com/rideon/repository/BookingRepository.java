package com.rideon.repository;

import com.rideon.entity.Booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Optional<Booking> findByBookingRef(String bookingRef);
    List<Booking> findByUserUserId(int userId);
    @Query("SELECT b FROM Booking b WHERE b.trip.operator.userId = :operatorId")
    List<Booking> getBookingsByOperatorId(int operatorId);
    @Query("SELECT b from Booking b WHERE b.bookingStatus = 'CANCELLED'")
    List<Booking> getCancelledBookings();
}