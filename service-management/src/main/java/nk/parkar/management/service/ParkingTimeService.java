package nk.parkar.management.service;


import nk.parkar.management.model.ParkingTime;

import java.util.List;

public interface ParkingTimeService {
    List<ParkingTime> queryTimeBySpaceId(Integer spaceId);
}
