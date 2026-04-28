package com.rideon.features.trip.dto;

import com.rideon.entity.Availability;

public class TripSeatResponse {
    private int tripSeatId;
    private String seatNo;
    private Availability availability; // or your enum type

   public TripSeatResponse() {}

   public TripSeatResponse(int tripSeatId,String seatNo, Availability availability) {
	super();
	this.tripSeatId = tripSeatId;
	this.seatNo = seatNo;
	this.availability = availability;
   }

   public String getSeatNo() {
	return seatNo;
   }

   public void setSeatNo(String seatNo) {
	this.seatNo = seatNo;
   }

   public Availability getAvailability() {
	return availability;
   }

   public void setAvailability(Availability availability) {
	this.availability = availability;
   }

   public int getTripSeatId() {
	return tripSeatId;
   }

   public void setTripSeatId(int tripSeatId) {
	this.tripSeatId = tripSeatId;
   }
   
   
   
   
}