package com.nkcs.nkparking.service;


import com.nkcs.nkparking.model.ParkingTime;

import java.util.List;

public interface ParkingTimeService {
    List<ParkingTime> queryTimeBySpaceId(Integer spaceId);
}
