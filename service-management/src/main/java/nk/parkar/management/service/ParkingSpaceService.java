package nk.parkar.management.service;

import nk.parkar.management.model.ParkingSpace;

import java.util.List;

public interface ParkingSpaceService {
    ParkingSpace querySpaceById(Integer spaceId);

    List<Integer> querySpaceByTime(Long startTime, Long endTime);

    ParkingSpace updateSelective(ParkingSpace parkingSpace);

    List<ParkingSpace> getAllSpaces();
}
