package com.rideon.entity;

import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "route")
public class Route {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int routeId;
	@NotBlank(message = "source is required")
	private String source;
	@NotBlank(message = "destination is required")
	private String destination;
	@Positive(message = "distance is required")
	private Integer distance;
	@NotNull(message = "estimated time is required")
	private LocalTime estimatedTime;
	@NotBlank(message = "status is required")
	private String status;
	
	@OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Trip> trips;
	
	public Route() {}

	public Route(String source, String destination, Integer distance, LocalTime estimatedTime, String status) {
		super();
		this.source = source;
		this.destination = destination;
		this.distance = distance;
		this.estimatedTime = estimatedTime;
		this.status = status;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public LocalTime getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(LocalTime estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Trip> getTrips() {
		return trips;
	}

	public void setTrips(List<Trip> trips) {
		this.trips = trips;
	}
	
	
	
}
