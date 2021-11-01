package nk.parkar.management.util.entity;

public class UnavailableTime {
    private String startTime;
    private String endTime;

    public UnavailableTime() {
        startTime = "undefined startTime";
        endTime = "undefined endTime";
    }

    public UnavailableTime(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "AvailableTime{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
