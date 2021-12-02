package nk.parkar.management.service;

import nk.parkar.management.model.ParkingSpace;

import java.util.List;

public interface ParkingSpaceService {
    ParkingSpace querySpaceById(Integer spaceId);

    List<Integer> querySpaceByTime(Long startTime, Long endTime);

    List<ParkingSpace> getAllSpaces();

    Integer update(ParkingSpace parkingSpace);

    Integer updateSelective(ParkingSpace parkingSpace);

    public boolean checkExist(Integer spaceId);
}
