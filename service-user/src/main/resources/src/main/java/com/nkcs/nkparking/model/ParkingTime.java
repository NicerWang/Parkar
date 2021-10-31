package com.nkcs.nkparking.model;

import java.util.Date;

public class ParkingTime {
    private Integer timeId;

    private Integer spaceId;

    private Date startTime;

    private Date endTime;

    public Integer getTimeId() {
        return timeId;
    }

    public void setTimeId(Integer timeId) {
        this.timeId = timeId;
    }

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "ParkingTime{" +
                "timeId=" + timeId +
                ", spaceId=" + spaceId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}