package com.rideon.features.trip.dto;


import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TripRequestDTO {

    @NotNull(message = "Departure date is required")
    private LocalDate departureDate;

    @NotNull(message = "Departure time is required")
    private LocalTime departureTime;

    @NotNull(message = "Arrival time is required")
    private LocalTime arrivalTime;

    @NotNull(message = "Fare is required")
    private Double fare;

    @NotNull(message = "Bus ID is required")
    private Integer busId;

    @NotNull(message = "Route ID is required")
    private Integer routeId;
    
    private List<PickDropPointRequest> pickDropPoints;

    public LocalDate getDepartureDate() { return departureDate; }
    public void setDepartureDate(LocalDate departureDate) { this.departureDate = departureDate; }

    public LocalTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalTime departureTime) { this.departureTime = departureTime; }

    public LocalTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public Double getFare() { return fare; }
    public void setFare(Double fare) { this.fare = fare; }

    public Integer getBusId() { return busId; }
    public void setBusId(Integer busId) { this.busId = busId; }

    public Integer getRouteId() { return routeId; }
    public void setRouteId(Integer routeId) { this.routeId = routeId; }
	public List<PickDropPointRequest> getPickDropPoints() {
		return pickDropPoints;
	}
	public void setPickDropPoints(List<PickDropPointRequest> pickDropPoints) {
		this.pickDropPoints = pickDropPoints;
	}
    
    
}
