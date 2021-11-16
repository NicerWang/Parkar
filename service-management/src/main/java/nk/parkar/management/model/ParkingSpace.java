package nk.parkar.management.model;

import io.swagger.models.auth.In;

public class ParkingSpace {
    private Integer spaceId;

    private Byte occupied;

    private Integer mode;

    private Byte ban;

    private Byte booked;

    private Integer floor;

    private Integer xCoordinate;

    private Integer yCoordinate;

    public Byte getBooked() {
        return booked;
    }

    public void setBooked(Byte booked) {
        this.booked = booked;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(Integer xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public Integer getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(Integer yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public Byte getOccupied() {
        return occupied;
    }

    public void setOccupied(Byte occupied) {
        this.occupied = occupied;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Byte getBan() {
        return ban;
    }

    public void setBan(Byte ban) {
        this.ban = ban;
    }
}