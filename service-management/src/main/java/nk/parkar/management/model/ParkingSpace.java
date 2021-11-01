package nk.parkar.management.model;

public class ParkingSpace {
    private Integer spaceId;

    private Byte occupied;

    private Integer mode;

    private Byte ban;

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