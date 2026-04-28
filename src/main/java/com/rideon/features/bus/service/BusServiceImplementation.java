package com.rideon.features.bus.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rideon.entity.Bus;
import com.rideon.entity.Policy;
import com.rideon.entity.Seat;
import com.rideon.exception.ResourceNotFoundException;
import com.rideon.exception.ValidationException;
import com.rideon.repository.BusRepository;
import com.rideon.repository.PolicyRepository;
import com.rideon.repository.SeatRepository;
import com.rideon.repository.UserRepository;

@Service
public class BusServiceImplementation implements BusService{
	
	@Autowired
	BusRepository busRepository;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	SeatRepository seatRepository;
	
	@Autowired
    private PolicyRepository policyRepository;
	
    public List<Bus> findAllBuses(int operatorId) {
    	return busRepository.findByOperatorId(operatorId);
    }

    public List<Bus> findAllActiveBuses() {
        return busRepository.findAllActiveBuses();
    }
   
    
    public Optional<Bus> findBusById(int id) {
    	return busRepository.findById(id);
    }
    
    public void deleteBusById(int id) {
    	busRepository.deactivateBus(id);
    }
    
    public int updateBusStatus(int id,String status) {
    	return busRepository.updateBusStatus(id,status);
    }
    
    public Bus createBusAndGenerateSeats(Bus bus) {
        Bus existBus = busRepository.findByBusNumber(bus.getBusNumber());

        if (existBus != null) {
            throw new ValidationException("Bus Number already exists");
        }

        Bus savedBus = busRepository.save(bus);

        int half = bus.getTotalSeat() / 2;
        boolean isSleeper = bus.getBusType().equals("SLEEPER");

        List<Seat> seats = IntStream.rangeClosed(1, bus.getTotalSeat())
                .mapToObj(i -> {
                    Seat seat = new Seat();
                    seat.setSeatNo("S" + i);
                    seat.setBus(savedBus);
                    seat.setBerthType(isSleeper && i <= half ? "LOWER" : "UPPER");
                    return seat;
                })
                .collect(Collectors.toList());

        seatRepository.saveAll(seats);

        return savedBus;
    }

    public List<Seat> getSeatsByBusId(int busId) {
        busRepository.findById(busId)
                .orElseThrow(() -> new ResourceNotFoundException("No bus found with id: " + busId));
        return seatRepository.findSeatsByBusId(busId);
    }

    public void assignPolicyToBus(int busId, int policyId) {

        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

        bus.getPolicies().add(policy);
        busRepository.save(bus);
    }

    public List<Policy> getPoliciesByBusId(int busId) {

        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));

        return bus.getPolicies();
    }
}