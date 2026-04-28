package com.rideon.entity;

import java.time.LocalTime;

import jakarta.persistence.*;

@Entity
@Table(name = "Pick_Droppoint")
public class PickDropPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Pick_drop_id")
    private Integer pickDropId;

    @Column(name = "points", nullable = false)
    private String points;

    @Enumerated(EnumType.STRING)
    @Column(name = "point_type", nullable = false)
    private PointType pointType;

    @Column(name = "arrival_time")
    private java.time.LocalTime arrivalTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public PickDropPoint() {}

	public PickDropPoint(String points, PointType pointType, LocalTime arrivalTime, Route route, Trip trip) {
		super();
		this.points = points;
		this.pointType = pointType;
		this.arrivalTime = arrivalTime;
		this.route = route;
		this.trip = trip;
	}

	public Integer getPickDropId() {
		return pickDropId;
	}

	public void setPickDropId(Integer pickDropId) {
		this.pickDropId = pickDropId;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public PointType getPointType() {
		return pointType;
	}

	public void setPointType(PointType pointType) {
		this.pointType = pointType;
	}

	public java.time.LocalTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(java.time.LocalTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}
    
}

    