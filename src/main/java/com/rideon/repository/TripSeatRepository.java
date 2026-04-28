package com.rideon.repository;

import com.rideon.entity.TripSeat;

import com.rideon.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TripSeatRepository extends JpaRepository<TripSeat, Integer> {

    List<TripSeat> findByTripTripIdAndAvailability(int tripId, Availability availability);

    @Query("select ts from TripSeat ts where ts.trip.tripId = :tripId and ts.tripSeatId in :seatIds")
    List<TripSeat> findByTripIdAndSeatIds(@Param("tripId") int tripId,@Param("seatIds") List<Integer> seatIds);

    @Modifying
    @Query("update TripSeat ts set ts.availability = 'BOOKED' where ts.tripSeatId = :id")
    int markBooked(@Param("id") int tripSeatId);

    @Modifying
    @Query("update TripSeat ts set ts.availability = 'AVAILABLE' where ts.tripSeatId = :id")
    int markAvailable(@Param("id") int tripSeatId);
    
    
    @Query("select ts from TripSeat ts where ts.trip.tripId = :tripId and ts.seat.seatNo in :seatNos")
    List<TripSeat> findByTripIdAndSeatNos(@Param("tripId") int tripId,@Param("seatNos") List<String> seatNos);
    
    @Query("""
            select ts from TripSeat ts
            where ts.trip.tripId = :tripId
            and ts.seat.seatNo in (
                select p.seatNo from Passenger p
                where p.booking.bookingRef = :bookingRef
            )
            """)
    List<TripSeat> findByTripIdAndBookingRef(@Param("tripId") int tripId,
                                              @Param("bookingRef") String bookingRef);
    
 
}