package com.rideon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rideon.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer>{
	Optional<Payment> findByBookingBookingId(int bookingId);
	boolean existsByPaymentRef(String paymentRef);
	Payment findByPaymentRef(String paymentRef);
}
