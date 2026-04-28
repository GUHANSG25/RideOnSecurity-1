package com.rideon.features.route.dto;

import java.time.LocalTime;

public class RouteResponse {
	private int routeId;
	private String source;
	private String destination;
	private int distance;
	private LocalTime estimatedTime;
	private String status;
	
	public RouteResponse() {
		
	}

	public RouteResponse(int routeId,String source, String destination, int distance, LocalTime estimatedTime, String status) {
		super();
		this.routeId = routeId;
		this.source = source;
		this.destination = destination;
		this.distance = distance;
		this.estimatedTime = estimatedTime;
		this.status = status;
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

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
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

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}
	
	
	
	
}
