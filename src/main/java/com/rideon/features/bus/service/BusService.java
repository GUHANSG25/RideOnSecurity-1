package com.rideon.features.bus.service;

import java.util.List;
import java.util.Optional;

import com.rideon.entity.Bus;
import com.rideon.entity.Policy;
import com.rideon.entity.Seat;

public interface BusService {
	 List<Bus> findAllBuses(int operatorId);
	 List<Bus> findAllActiveBuses();
//	 Bus saveBuses(Bus bus);
	 Bus createBusAndGenerateSeats(Bus bus);
	 Optional<Bus> findBusById(int id);
	 void deleteBusById(int id);
	 int updateBusStatus(int id,String status);
//	 public List<Seat> addSeats(int busId, List<Seat> seat);
	 public List<Seat> getSeatsByBusId(int busId);
	 void assignPolicyToBus(int busId, int policyId);
	 List<Policy> getPoliciesByBusId(int busId);
}
