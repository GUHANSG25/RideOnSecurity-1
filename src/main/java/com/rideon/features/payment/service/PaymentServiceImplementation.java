package com.rideon.features.payment.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.rideon.entity.*;
import com.rideon.exception.ResourceNotFoundException;
import com.rideon.exception.ValidationException;
import com.rideon.features.booking.dto.BookingResponse;
import com.rideon.features.booking.dto.PassengerDTO;
import com.rideon.features.booking.service.BookingService;
import com.rideon.features.payment.dto.RazorpayOrderRequest;
import com.rideon.features.payment.dto.RazorpayOrderResponse;
import com.rideon.features.payment.dto.RazorpayVerifyRequest;
import com.rideon.repository.*;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImplementation implements PaymentService {

    @Autowired 
    private BookingRepository bookingRepository;
    @Autowired 
    private TripSeatRepository tripSeatRepository;
    @Autowired 
    private PassengerRepository passengerRepository;
    @Autowired 
    private PaymentRepository paymentRepository;
    @Autowired 
    private BookingService bookingService;

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    
    @Override
    public RazorpayOrderResponse createOrder(RazorpayOrderRequest request) {
        if (request.getTotalFare() == null || request.getTotalFare() <= 0) {
            throw new ValidationException("Invalid booking amount");
        }

        try {
            RazorpayClient client = new RazorpayClient(keyId, keySecret);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", (int) (request.getTotalFare() * 100)); 
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            orderRequest.put("payment_capture", 1);

            Order order = client.orders.create(orderRequest);

            RazorpayOrderResponse res = new RazorpayOrderResponse();
            res.setRazorpayOrderId(order.get("id"));
            res.setAmount(request.getTotalFare());
            res.setCurrency("INR");
            res.setKeyId(keyId);
            return res;

        } catch (RazorpayException e) {
            throw new ValidationException("Failed to create Razorpay order: " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public BookingResponse verifyAndConfirm(RazorpayVerifyRequest request, com.rideon.entity.User user) {
        if (request.getRazorpayOrderId() == null
                || request.getRazorpayPaymentId() == null
                || request.getRazorpaySignature() == null) {
            throw new ValidationException("Missing Razorpay payment fields");
        }

        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", request.getRazorpayOrderId());
            attributes.put("razorpay_payment_id", request.getRazorpayPaymentId());
            attributes.put("razorpay_signature", request.getRazorpaySignature());
            Utils.verifyPaymentSignature(attributes, keySecret);
        } catch (RazorpayException e) {
            bookingService.releaseLockedSeats(request.getSeatIds());
            throw new ValidationException("Payment signature invalid: " + e.getMessage());
        }

        List<TripSeat> tripSeats = tripSeatRepository.findAllById(request.getSeatIds());
        if (tripSeats.isEmpty()) {
            throw new ResourceNotFoundException("Seats not found");
        }
        Trip trip = tripSeats.get(0).getTrip();


        String bookingRef = "BK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Booking booking = new Booking();
        booking.setBookingRef(bookingRef);
        booking.setUser(user);
        booking.setTrip(trip);
        booking.setPickupPoint(request.getPickupPoint());
        booking.setDropPoint(request.getDropPoint());
//        booking.setBaseFare(request.getBaseFare());
//        booking.setConvenienceFee(request.getConvenienceFee());
        booking.setTotalFare(request.getTotalFare());
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setBookingDate(LocalDateTime.now());
        booking.setTravelDate(trip.getDepartureDate());
        bookingRepository.save(booking);

        // 6. Save passengers
        List<Passenger> passengers = request.getPassengers().stream().map(dto -> {
            Passenger p = new Passenger();
            p.setBooking(booking);
            p.setPassengerName(dto.getPassengerName());
            p.setAge(dto.getAge());
            p.setGender(dto.getGender());
            p.setSeatNo(dto.getSeatNo());
            return p;
        }).collect(Collectors.toList());
        passengerRepository.saveAll(passengers);

        com.rideon.entity.Payment payment = new com.rideon.entity.Payment();
        payment.setBooking(booking);
        payment.setPaymentRef(request.getRazorpayPaymentId());
        payment.setAmount(request.getTotalFare());
        payment.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "RAZORPAY");
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);

        tripSeats.forEach(s -> s.setAvailability(Availability.BOOKED));
        tripSeatRepository.saveAll(tripSeats);

        return buildBookingResponse(booking, passengers, payment);
    }

    private BookingResponse buildBookingResponse(Booking b, List<Passenger> passengers,
                                                  com.rideon.entity.Payment payment) {
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
//        res.setBaseFare(b.getBaseFare());
//        res.setConvenienceFee(b.getConvenienceFee());
        res.setTotalFare(b.getTotalFare());
        res.setPaymentMethod(payment.getPaymentMethod());
        res.setPaymentStatus(payment.getPaymentStatus().name());
        res.setRazorpayPaymentId(payment.getPaymentRef());

        List<PassengerDTO> passengerDTOs = passengers.stream().map(p -> {
            PassengerDTO dto = new PassengerDTO();
            dto.setPassengerName(p.getPassengerName());
            dto.setAge(p.getAge());
            dto.setGender(p.getGender());
            dto.setSeatNo(p.getSeatNo());
            return dto;
        }).collect(Collectors.toList());

        res.setPassengers(passengerDTOs);
        res.setSeatNumbers(passengerDTOs.stream().map(PassengerDTO::getSeatNo).collect(Collectors.toList()));
        return res;
    }
}