package com.rideon.features.trip.controller;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rideon.entity.PickDropPoint;
import com.rideon.entity.Trip;
import com.rideon.entity.TripStatus;
import com.rideon.entity.User;
import com.rideon.exception.ResourceNotFoundException;
import com.rideon.features.trip.dto.PickDropPointResponse;
import com.rideon.features.trip.dto.TripRequestDTO;
import com.rideon.features.trip.dto.TripResponse;
import com.rideon.features.trip.dto.TripSeatResponse;
import com.rideon.features.trip.service.TripService;
import com.rideon.repository.PickDropPointRepository;
import com.rideon.repository.UserRepository;
import com.rideon.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    TripService tripService;
    
    @Autowired
    PickDropPointRepository pickDropPointRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    JwtUtil jwtUtil;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_OPERATOR')")
    public ResponseEntity<List<TripResponse>> getTripsByOperatorId(HttpServletRequest httpRequest) {
    	String authHeader = httpRequest.getHeader("Authorization");
        String token = authHeader.substring(7);
        String mobile = jwtUtil.getMobileFromToken(token);
        int operatorId = userRepository.findByMobile(mobile).get().getUserId();
        
        List<TripResponse> trips = tripService.findTripsByOperatorId(operatorId);
        if (trips.isEmpty()) {
            throw new ResourceNotFoundException("No trips found");
        }
        return new ResponseEntity<>(trips, HttpStatus.OK);
    }
    
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_OPERATOR')")
    public ResponseEntity<TripResponse> createTrip(@Valid @RequestBody TripRequestDTO tripRequest,HttpServletRequest httpRequest) {
        
    	String authHeader = httpRequest.getHeader("Authorization");
        String token = authHeader.substring(7);
        String mobile = jwtUtil.getMobileFromToken(token);
        Optional<User> operator = userRepository.findByMobile(mobile);
        
    	TripResponse savedTrip = tripService.saveTrip(tripRequest,operator);
   
        return new ResponseEntity<>(savedTrip, HttpStatus.CREATED);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<TripResponse>> searchTrip(@RequestParam String source
    		,@RequestParam String destination,@RequestParam LocalDate date){
    	List<TripResponse> searchTrip = tripService.searchTrips(source,destination,date);
    	
    	if (searchTrip.isEmpty()) {
    	    throw new ResourceNotFoundException("No trips found");
    	}
    	
    	return new ResponseEntity<>(searchTrip,HttpStatus.OK);
    }
    
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_OPERATOR')")
    public ResponseEntity<String> updateTripStatus(@PathVariable int id,@RequestParam TripStatus status){
    	int updated = tripService.updateTripStatus(id, status);
    	
    	if(updated < 0) {
    		return new ResponseEntity<>("Updation failed",HttpStatus.BAD_REQUEST);
    	}
    	return new ResponseEntity<>("Trip status updated successfully",HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_OPERATOR')")
    public ResponseEntity<String> deleteTrip(@PathVariable int id){
    	int deleted = tripService.deleteTripById(id);
    	
    	if(deleted < 0) {
    		return new ResponseEntity<>("Deletion failed",HttpStatus.BAD_REQUEST);
    	}
    	return new ResponseEntity<>("Trip deactivated successfully",HttpStatus.OK);
    }
    
    @GetMapping("/{id}/seats")
    public ResponseEntity<List<TripSeatResponse>> getAllSeatsByTripId(@PathVariable int id){
    	List<TripSeatResponse> tripSeat = tripService.getAllSeatsByTrip(id);
    	
    	if(tripSeat.isEmpty()) {
    		throw new ResourceNotFoundException("Seats no found");
    	}
    	
    	return new ResponseEntity<>(tripSeat,HttpStatus.OK);
    }
    
    @GetMapping("/{id}/availableseats")
    @PreAuthorize("hasAuthority('ROLE_OPERATOR') or hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<List<TripSeatResponse>> getAvailableSeats(@PathVariable int id){
    	List<TripSeatResponse> tripSeat = tripService.getAvailableSeats(id);
    	
    	if(tripSeat.isEmpty()) {
    		throw new ResourceNotFoundException("Seats no found");
    	}
    	
    	return new ResponseEntity<>(tripSeat,HttpStatus.OK);
    }
    
    @GetMapping("/{id}/points")
    public ResponseEntity<List<PickDropPointResponse>> getPickDropPoints(@PathVariable int id) {
        List<PickDropPoint> points = pickDropPointRepository.findByTripTripId(id);

        if (points.isEmpty()) {
            throw new ResourceNotFoundException("No pickup/drop points found for trip " + id);
        }

        List<PickDropPointResponse> response = points.stream().map(p ->
                new PickDropPointResponse(
                        p.getPickDropId(),
                        p.getPoints(),
                        p.getPointType(),
                        p.getArrivalTime()
                )
        ).toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_OPERATOR') or hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<Optional<Trip>> getTripById(@PathVariable int id) {
        Optional<Trip> trip = tripService.findTripById(id);
        return ResponseEntity.ok(trip);
    }
    

}
