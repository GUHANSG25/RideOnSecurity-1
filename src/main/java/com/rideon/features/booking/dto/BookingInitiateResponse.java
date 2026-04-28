package com.rideon.features.booking.dto;

import java.util.List;

public class BookingInitiateResponse {
	private List<Integer>  seatIds;         // echo back for frontend
    private List<String> seatNumbers;
    private String      pickupPoint;
    private String      dropPoint;
    private double      baseFare;
    private double      convenienceFee;
    private double      totalFare;
	public List<Integer> getSeatIds() {
		return seatIds;
	}
	public void setSeatIds(List<Integer> seatIds) {
		this.seatIds = seatIds;
	}
	public List<String> getSeatNumbers() {
		return seatNumbers;
	}
	public void setSeatNumbers(List<String> seatNumbers) {
		this.seatNumbers = seatNumbers;
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
	public double getBaseFare() {
		return baseFare;
	}
	public void setBaseFare(double baseFare) {
		this.baseFare = baseFare;
	}
	public double getConvenienceFee() {
		return convenienceFee;
	}
	public void setConvenienceFee(double convenienceFee) {
		this.convenienceFee = convenienceFee;
	}
	public double getTotalFare() {
		return totalFare;
	}
	public void setTotalFare(double totalFare) {
		this.totalFare = totalFare;
	}

    
}