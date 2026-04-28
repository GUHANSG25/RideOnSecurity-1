package com.rideon.features.booking.service;

import com.rideon.entity.*;

import com.rideon.exception.ResourceNotFoundException;
import com.rideon.exception.ValidationException;
import com.rideon.features.booking.dto.*;
import com.rideon.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImplementation implements BookingService {

    @Autowired 
    private BookingRepository bookingRepository;
    @Autowired 
    private TripSeatRepository tripSeatRepository;
    @Autowired 
    private PassengerRepository passengerRepository;
    @Autowired 
    private PaymentRepository paymentRepository;

    @Override
    @Transactional
    public BookingInitiateResponse lockSeats(int userId, BookingInitiateRequest request) {
        List<TripSeat> tripSeats = tripSeatRepository.findAllById(request.getSeatIds());

        if (tripSeats.size() != request.getSeatIds().size()) {
            throw new ResourceNotFoundException("One or more seats not found");
        }

        for (TripSeat seat : tripSeats) {
            if (seat.getAvailability() != Availability.AVAILABLE) {
                throw new ValidationException("Seat " + seat.getSeat().getSeatNo() + " is no longer available.");
            }
            seat.setAvailability(Availability.LOCKED);
        }
        tripSeatRepository.saveAll(tripSeats);

        Trip trip = tripSeats.get(0).getTrip();
        double baseFare = trip.getFare() * tripSeats.size();
        double convenienceFee = 35.0;
        double totalFare = baseFare + convenienceFee;

        BookingInitiateResponse response = new BookingInitiateResponse();
        response.setSeatIds(request.getSeatIds());
        response.setSeatNumbers(tripSeats.stream()
                .map(ts -> ts.getSeat().getSeatNo())
                .toList());        
        response.setPickupPoint(request.getPickupPoint());
        response.setDropPoint(request.getDropPoint());
        response.setBaseFare(baseFare);
        response.setConvenienceFee(convenienceFee);
        response.setTotalFare(totalFare);
        return response;
    }

    @Override
    @Transactional
    public void releaseLockedSeats(List<Integer> seatIds) {
        List<TripSeat> seats = tripSeatRepository.findAllById(seatIds);
        seats.forEach(s -> s.setAvailability(Availability.AVAILABLE));
        tripSeatRepository.saveAll(seats);
    }

    @Override
    public BookingResponse getBookingByRef(String bookingRef) {
        Booking booking = bookingRepository.findByBookingRef(bookingRef)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingRef));
        return mapToResponse(booking);
    }

    @Override
    public List<BookingResponse> getMyBookings(int userId) {
        return bookingRepository.findByUserUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public List<BookingResponse> getBookingsByOperatorId(int operatorId){
    	return bookingRepository.getBookingsByOperatorId(operatorId)
    			.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public List<BookingResponse> getCancelledBookings(){
    	return bookingRepository.getCancelledBookings()
    			.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public BookingResponse cancelBooking(String bookingRef, int userId) {
        Booking booking = bookingRepository.findByBookingRef(bookingRef)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getUser().getUserId() != userId) {
            throw new ValidationException("You are not authorized to cancel this booking");
        }
        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new ValidationException("Booking is already cancelled");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        List<String> seatNos = passengerRepository
                .findByBookingBookingId(booking.getBookingId())
                .stream()
                .map(Passenger::getSeatNo)
                .collect(Collectors.toList());

        List<TripSeat> tripSeats = tripSeatRepository
                .findByTripIdAndSeatNos(booking.getTrip().getTripId(), seatNos);

        tripSeats.forEach(ts -> ts.setAvailability(Availability.AVAILABLE));
        tripSeatRepository.saveAll(tripSeats);

        return mapToResponse(booking);
    }

    private BookingResponse mapToResponse(Booking b) {
        BookingResponse res = new BookingResponse();
        res.setBookingRef(b.getBookingRef());
        res.setBookingStatus(b.getBookingStatus().name());
        res.setBookingDate(b.getBookingDate());
        res.setTravelDate(b.getTravelDate());
        res.setSource(b.getTrip().getRoute().getSource());
        res.setDestination(b.getTrip().getRoute().getDestination());
        res.setDepartureTime(b.getTrip().getDepartureTime());
        res.setArrivalTime(b.getTrip().getArrivalTime());
        res.setBusName(b.getTrip().getBus().getBusName());
        res.setBusNumber(b.getTrip().getBus().getBusNumber());
        res.setBusType(b.getTrip().getBus().getBusType());
        res.setPickupPoint(b.getPickupPoint());
        res.setDropPoint(b.getDropPoint());
        res.setTotalFare(b.getTotalFare());

        paymentRepository.findByBookingBookingId(b.getBookingId()).ifPresent(pay -> {
            res.setPaymentMethod(pay.getPaymentMethod());
            res.setPaymentStatus(pay.getPaymentStatus().name());
            res.setRazorpayPaymentId(pay.getPaymentRef());
        });

        List<PassengerDTO> passengerDTOs = passengerRepository
                .findByBookingBookingId(b.getBookingId())
                .stream()
                .map(p -> {
                    PassengerDTO dto = new PassengerDTO();
                    dto.setPassengerName(p.getPassengerName());
                    dto.setAge(p.getAge());
                    dto.setGender(p.getGender());
                    dto.setSeatNo(p.getSeatNo());
                    return dto;
                }).collect(Collectors.toList());

        res.setPassengers(passengerDTOs);
        res.setSeatNumbers(passengerDTOs.stream()
                .map(PassengerDTO::getSeatNo)
                .collect(Collectors.toList()));
        return res;
    }
    
    
}