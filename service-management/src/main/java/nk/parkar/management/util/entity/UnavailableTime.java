package nk.parkar.management.util.entity;

public class UnavailableTime {
    private long startTime;
    private long endTime;

    public UnavailableTime() {
        startTime = -1;
        endTime = -1;
    }

    public UnavailableTime(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
