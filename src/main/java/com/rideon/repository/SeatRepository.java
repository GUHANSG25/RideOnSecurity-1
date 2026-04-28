package com.rideon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rideon.entity.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {
	@Query("SELECT s FROM Seat s WHERE s.bus.busId = :busId")
    List<Seat> findSeatsByBusId(@Param("busId") int busId);
}
