package com.rideon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Integer tripId;

    @Column(name = "departure_date", nullable = false)
    @NotNull(message = "Departure date is required")
    private LocalDate departureDate;

    @Column(name = "departure_time", nullable = false)
    @NotNull(message = "Departure time is required")
    private LocalTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    @NotNull(message = "Arrival time is required")
    private LocalTime arrivalTime;

    @Column(name = "fare",nullable = false)
    @NotNull(message = "Fare is required")
    private Double fare;

    @ManyToOne
    @JoinColumn(name = "bus_id", nullable = false)
    @NotNull(message = "Bus is required")
    private Bus bus;

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    @NotNull(message = "Route is required")
    private Route route;

    @Enumerated(EnumType.STRING)
    @Column(name = "trip_status", nullable = false)
    private TripStatus tripStatus = TripStatus.ACTIVE;
    
    @JsonIgnore
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TripSeat> tripSeats;
    
    @ManyToOne
    @JoinColumn(name = "operator_id", nullable = false)
    @NotNull(message = "Operator is required")
    private User operator;

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public TripStatus getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(TripStatus tripStatus) {
        this.tripStatus = tripStatus;
    }
    
    public List<TripSeat> getTripSeats() {
        return tripSeats;
    }

    public void setTripSeats(List<TripSeat> tripSeats) {
        this.tripSeats = tripSeats;
    }
    
    public User getOperator() {
        return operator;
    }
    public void setOperator(User operator) {
        this.operator = operator;
    }
}
