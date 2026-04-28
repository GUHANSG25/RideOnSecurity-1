package com.rideon.repository;

import com.rideon.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger, Integer> {
    List<Passenger> findByBookingBookingId(int bookingId);
}