package com.rideon.features.trip.dto;

import com.rideon.entity.PointType;
import java.time.LocalTime;

public class PickDropPointRequest {
    private String points;
    private PointType pointType;   // SOURCE or DESTINATION
    private LocalTime arrivalTime;

    public PickDropPointRequest() {}

    public String getPoints() { return points; }
    public void setPoints(String points) { this.points = points; }

    public PointType getPointType() { return pointType; }
    public void setPointType(PointType pointType) { this.pointType = pointType; }

    public LocalTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalTime arrivalTime) { this.arrivalTime = arrivalTime; }
}