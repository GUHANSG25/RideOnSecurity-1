package com.rideon.repository;

import java.time.LocalDate;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rideon.entity.Trip;
import com.rideon.entity.TripStatus;
import com.rideon.features.trip.dto.TripSeatResponse;

import jakarta.transaction.Transactional;

public interface TripRepository extends JpaRepository<Trip, Integer> {
	@Query("select t from Trip t where t.route.source = :source and t.route.destination = :destination and t.departureDate = :date AND t.tripStatus = 'ACTIVE'")
    List<Trip> searchTrips(@Param("source") String source,
                           @Param("destination") String destination,
                           @Param("date") LocalDate date);
	
	@Modifying
    @Transactional
    @Query("update Trip t set t.tripStatus = :status where t.tripId = :tripId")
    int updateTripStatus(@Param("tripId") int tripId, @Param("status") TripStatus status);
	
	@Modifying
	@Transactional
	@Query("update Trip t set t.tripStatus = 'InActive' where t.tripId = :id")
	int deleteTrip(@Param("id") int id);
	
	@Query("select new com.rideon.features.trip.dto.TripSeatResponse(ts.tripSeatId, s.seatNo, ts.availability) from TripSeat ts join ts.seat s where ts.trip.tripId = :id")
	List<TripSeatResponse> getAllSeatsByTripId(@Param("id") int id);

	@Query("select new com.rideon.features.trip.dto.TripSeatResponse(ts.tripSeatId, s.seatNo, ts.availability) from TripSeat ts join ts.seat s where ts.trip.tripId = :id and ts.availability = 'AVAILABLE'")
	List<TripSeatResponse> getAvailableSeats(@Param("id") int id);
	
	@Query("SELECT t FROM Trip t WHERE t.operator.userId = :operatorId")
	List<Trip> findTripsByOperatorId(@Param("operatorId") int operatorId);
	
}
