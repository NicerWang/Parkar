package com.nkcs.nkparking.service;

import java.util.List;

public interface ParkingSpaceService {
    Object querySpaceById(Integer spaceId);

    List<Integer> querySpaceByTime(Long startTime, Long endTime);
}
