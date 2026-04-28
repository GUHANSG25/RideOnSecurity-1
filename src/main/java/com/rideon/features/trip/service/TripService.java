package com.rideon.features.trip.service;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import com.rideon.entity.Trip;
//import com.rideon.entity.TripSeat;
import com.rideon.entity.TripStatus;
import com.rideon.entity.User;
import com.rideon.features.trip.dto.TripRequestDTO;
import com.rideon.features.trip.dto.TripResponse;
import com.rideon.features.trip.dto.TripSeatResponse;

public interface TripService {
	 TripResponse saveTrip(TripRequestDTO tripRequest,Optional<User> operator);
	 List<TripResponse> findTripsByOperatorId(int operatorId);
	 List<TripResponse> searchTrips(String source, String destination, LocalDate date);
	 int updateTripStatus(int id, TripStatus status);
	 int deleteTripById(int tripId);

	 // Trip seat management
     void initializeTripSeats(Trip trip);       
	 List<TripSeatResponse> getAvailableSeats(int tripId);
	 List<TripSeatResponse> getAllSeatsByTrip(int tripId);
	 Optional<Trip> findTripById(int id);
}
