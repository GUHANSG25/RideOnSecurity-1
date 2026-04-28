package com.rideon.features.bus.controller;

import java.util.List;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rideon.entity.Bus;
import com.rideon.entity.Seat;
import com.rideon.entity.User;
import com.rideon.exception.ResourceNotFoundException;
import com.rideon.features.bus.service.BusService;
import com.rideon.features.policy.service.PolicyService;
import com.rideon.repository.UserRepository;
import com.rideon.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/buses")
public class BusController {

    @Autowired
    BusService busService;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    PolicyService policyService;
    
    @Autowired
    JwtUtil jwtUtil;
    
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_OPERATOR')")
    public ResponseEntity<Bus> createBusAndGenerateSeats(@Valid @RequestBody Bus bus) {
        Bus newBus = busService.createBusAndGenerateSeats(bus);
        return new ResponseEntity<>(newBus, HttpStatus.CREATED);
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_OPERATOR')")
    public ResponseEntity<List<Bus>> findAllBuses(HttpServletRequest httpRequest) {
    	String authHeader = httpRequest.getHeader("Authorization");
        String token = authHeader.substring(7);
        String mobile = jwtUtil.getMobileFromToken(token);
        
        Optional<User> operator = userRepository.findByMobile(mobile);
        
        List<Bus> buses = busService.findAllBuses(operator.get().getUserId());
        
        if (buses.isEmpty()) {
            throw new ResourceNotFoundException("No buses found");
        }
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Bus> findByBusId(@PathVariable Integer id) {
        Bus bus = busService.findBusById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No bus found with id: " + id));
        return new ResponseEntity<>(bus, HttpStatus.OK);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('ROLE_OPERATOR')")
    public ResponseEntity<List<Bus>> findAllActiveBuses() {
        List<Bus> buses = busService.findAllActiveBuses();
        if (buses.isEmpty()) {
            throw new ResourceNotFoundException("No active buses found");
        }
        return new ResponseEntity<>(buses, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_OPERATOR')")
    public ResponseEntity<String> updateBusStatus(@PathVariable int id, @RequestParam String status) {
    	busService.findBusById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No bus found with id: " + id));
        int updated = busService.updateBusStatus(id, status);
        if(updated > 0) {
        	return ResponseEntity.ok("Updated Successful");
        }else {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update failed");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_OPERATOR')")
    public ResponseEntity<Bus> deleteBusById(@PathVariable int id) {
    	Bus bus = busService.findBusById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No bus found with id: " + id));
        busService.deleteBusById(id);
        return ResponseEntity.ok(bus);
    }
    


    @GetMapping("/buses/{busId}/seats")
    @PreAuthorize("hasAuthority('ROLE_OPERATOR')")
    public ResponseEntity<List<Seat>> getSeatsByBusId(@PathVariable int busId) {
        List<Seat> seats = busService.getSeatsByBusId(busId);
        if (seats.isEmpty()) {
            throw new ResourceNotFoundException("No seats found for bus id: " + busId);
        }
        return new ResponseEntity<>(seats, HttpStatus.OK);
    }
    
}
