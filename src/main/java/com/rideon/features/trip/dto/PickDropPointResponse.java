package com.rideon.features.trip.dto;

import com.rideon.entity.PointType;
import java.time.LocalTime;

public class PickDropPointResponse {
    private Integer pickDropId;
    private String points;
    private PointType pointType;
    private LocalTime arrivalTime;

    public PickDropPointResponse() {}

    public PickDropPointResponse(Integer pickDropId, String points,
                                  PointType pointType, LocalTime arrivalTime) {
        this.pickDropId = pickDropId;
        this.points = points;
        this.pointType = pointType;
        this.arrivalTime = arrivalTime;
    }

    public Integer getPickDropId() { return pickDropId; }
    public void setPickDropId(Integer pickDropId) { this.pickDropId = pickDropId; }

    public String getPoints() { return points; }
    public void setPoints(String points) { this.points = points; }

    public PointType getPointType() { return pointType; }
    public void setPointType(PointType pointType) { this.pointType = pointType; }

    public LocalTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalTime arrivalTime) { this.arrivalTime = arrivalTime; }
}