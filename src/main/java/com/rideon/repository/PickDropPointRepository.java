package com.rideon.repository;

import com.rideon.entity.PickDropPoint;
import com.rideon.entity.PointType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PickDropPointRepository extends JpaRepository<PickDropPoint, Integer> {

    // Get all pickup points for a trip
    List<PickDropPoint> findByTripTripIdAndPointType(int tripId, PointType pointType);

    // Get all points (both pickup + drop) for a trip
    List<PickDropPoint> findByTripTripId(int tripId);
}