package com.rideon.features.trip.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rideon.entity.Availability;
import com.rideon.entity.Bus;
import com.rideon.entity.PickDropPoint;
import com.rideon.entity.Route;
import com.rideon.entity.Seat;
import com.rideon.entity.Trip;
import com.rideon.entity.TripSeat;
import com.rideon.entity.TripStatus;
import com.rideon.entity.User;
import com.rideon.exception.ResourceNotFoundException;
import com.rideon.exception.ValidationException;
import com.rideon.features.trip.dto.PickDropPointRequest;
import com.rideon.features.trip.dto.TripRequestDTO;
import com.rideon.features.trip.dto.TripResponse;
import com.rideon.features.trip.dto.TripSeatResponse;
import com.rideon.repository.BusRepository;
import com.rideon.repository.PickDropPointRepository;
import com.rideon.repository.RouteRepository;
import com.rideon.repository.SeatRepository;
import com.rideon.repository.TripRepository;
import com.rideon.repository.TripSeatRepository;

import jakarta.transaction.Transactional;

@Service
public class TripServiceImplementation implements TripService{
	
	@Autowired
	TripRepository tripRepository;
	
	@Autowired
    BusRepository busRepository;

    @Autowired
    TripSeatRepository tripSeatRepository;
    
    @Autowired
    RouteRepository routeRepository;
    
    @Autowired
    SeatRepository seatRepository;
    
    @Autowired
    PickDropPointRepository pickDropPointRepository;

    @Transactional
    public TripResponse saveTrip(TripRequestDTO tripRequest,Optional<User> operator) {
    	Bus bus = busRepository.findById(tripRequest.getBusId())
    			.orElseThrow(() -> new ResourceNotFoundException("Invalid Bus Id"));
    	Route route = routeRepository.findById(tripRequest.getRouteId())
    			.orElseThrow(() -> new ResourceNotFoundException("Invalid Route Id"));
    	
    	User tripOperator = operator
                .orElseThrow(() -> new ResourceNotFoundException("Operator not found"));
    	
    	Trip trip = new Trip();
    	trip.setDepartureDate(tripRequest.getDepartureDate());
    	trip.setDepartureTime(tripRequest.getDepartureTime());
    	trip.setArrivalTime(tripRequest.getArrivalTime());
    	trip.setFare(tripRequest.getFare());
    	trip.setBus(bus);
    	trip.setRoute(route);
    	trip.setOperator(tripOperator);
    	
        Trip savedTrip = tripRepository.save(trip);
        
        initializeTripSeats(savedTrip);
        
        if (tripRequest.getPickDropPoints() != null && !tripRequest.getPickDropPoints().isEmpty()) {
            for (PickDropPointRequest p : tripRequest.getPickDropPoints()) {
                PickDropPoint point = new PickDropPoint();
                point.setPoints(p.getPoints());
                point.setPointType(p.getPointType());
                point.setArrivalTime(p.getArrivalTime());
                point.setRoute(route);
                point.setTrip(savedTrip);
                pickDropPointRepository.save(point);
            }
        }
        
        
        return this.mapToTripResponse(savedTrip);
    }
	
	public List<TripResponse> findTripsByOperatorId(int operatorId){
		List<Trip> trips = tripRepository.findTripsByOperatorId(operatorId);
		return trips.stream().map(this::mapToTripResponse).toList();
	}
	
	public void initializeTripSeats(Trip trip) {
        Bus bus = trip.getBus();
        
        List<Seat> seats = seatRepository.findSeatsByBusId(bus.getBusId());

        for (Seat seat : seats) {
            TripSeat tripSeat = new TripSeat(trip, seat, Availability.AVAILABLE);
            tripSeatRepository.save(tripSeat);
        }
    }
	
	public List<TripResponse> searchTrips(String source,String destination,LocalDate date){
		if(source.isEmpty() || destination.isEmpty()) {
			throw new ResourceNotFoundException("Source and destination should not be empty");
		}
		if(date.isBefore(LocalDate.now())) {
			throw new ValidationException("Date should be future");
		}
		
		List<Trip> trips = tripRepository.searchTrips(source, destination, date);
		
		
		return trips.stream().map(this::mapToTripResponse).toList();		
	}
	
	private TripResponse mapToTripResponse(Trip trip) {
	    TripResponse response = new TripResponse();
	    response.setDepartureDate(trip.getDepartureDate());
	    response.setDepartureTime(trip.getDepartureTime());
	    response.setArrivalTime(trip.getArrivalTime());
	    response.setFare(trip.getFare());
	    response.setBusName(trip.getBus().getBusName());
	    response.setBusNumber(trip.getBus().getBusNumber());
	    response.setSource(trip.getRoute().getSource());
	    response.setDestination(trip.getRoute().getDestination());
	    response.setOperatorName(trip.getOperator().getUserName());
	    response.setBusType(trip.getBus().getBusType());
	    response.setAmenity(trip.getBus().getAmenity());
	    response.setTripId(trip.getTripId());
	    return response;
	}
	
	public int updateTripStatus(int id, TripStatus status) {
		tripRepository.findById(id).
				orElseThrow(() -> new ResourceNotFoundException("Trip not found with id " + id));
		
		return tripRepository.updateTripStatus(id, status);
	}
	
	public int deleteTripById(int id) {
		tripRepository.findById(id).
		        orElseThrow(() -> new ResourceNotFoundException("Trip not found with id " + id));
		
		return tripRepository.deleteTrip(id);
	}
	
	public List<TripSeatResponse> getAllSeatsByTrip(int id) {
		return tripRepository.getAllSeatsByTripId(id);
	}
	
	public List<TripSeatResponse> getAvailableSeats(int id) {
		return tripRepository.getAvailableSeats(id);
	}
	
	public Optional<Trip> findTripById(int id) {
		return tripRepository.findById(id);
	}
	
}
