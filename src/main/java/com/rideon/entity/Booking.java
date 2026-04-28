package com.rideon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private int bookingId;

    @Column(name = "booking_ref" ,unique = true, nullable = false)
    @NotBlank(message = "Booking reference is required")
    private String bookingRef;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    @NotNull(message = "Trip is required")
    private Trip trip;

    @Column(name = "booking_date")
    @NotNull(message = "Booking Date is required")
    private LocalDateTime bookingDate;
    
    @Column(name = "travel_date")
    @NotNull(message = "Travel Date is required")
    private LocalDate travelDate;
    
    @Column(name = "pickup_point")
    @NotBlank(message = "Pickup point is required")
    private String pickupPoint;
    
    @Column(name = "drop_point")
    @NotBlank(message = "Drop point is required")
    private String dropPoint;

    @Column(name = "total_fare")
    private double totalFare;
    
    @Column(name = "offer_code")
    private String offerCode;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    
    public Booking() {}

	public Booking(String bookingRef, User user, Trip trip, LocalDateTime bookingDate, LocalDate travelDate,
			String pickupPoint, String dropPoint, double totalFare, String offerCode, BookingStatus bookingStatus) {
		super();
		this.bookingRef = bookingRef;
		this.user = user;
		this.trip = trip;
		this.bookingDate = bookingDate;
		this.travelDate = travelDate;
		this.pickupPoint = pickupPoint;
		this.dropPoint = dropPoint;
		this.totalFare = totalFare;
		this.offerCode = offerCode;
		this.bookingStatus = bookingStatus;
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public String getBookingRef() {
		return bookingRef;
	}

	public void setBookingRef(String bookingRef) {
		this.bookingRef = bookingRef;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	public LocalDateTime getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(LocalDateTime bookingDate) {
		this.bookingDate = bookingDate;
	}

	public LocalDate getTravelDate() {
		return travelDate;
	}

	public void setTravelDate(LocalDate travelDate) {
		this.travelDate = travelDate;
	}

	public String getPickupPoint() {
		return pickupPoint;
	}

	public void setPickupPoint(String pickupPoint) {
		this.pickupPoint = pickupPoint;
	}

	public String getDropPoint() {
		return dropPoint;
	}

	public void setDropPoint(String dropPoint) {
		this.dropPoint = dropPoint;
	}

	public double getTotalFare() {
		return totalFare;
	}

	public void setTotalFare(double totalFare) {
		this.totalFare = totalFare;
	}

	public String getOfferCode() {
		return offerCode;
	}

	public void setOfferCode(String offerCode) {
		this.offerCode = offerCode;
	}

	public BookingStatus getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(BookingStatus bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
    
	
    

}