package com.nkcs.nkparking.service.Impl;

import com.nkcs.nkparking.mapper.ParkingTimeMapper;
import com.nkcs.nkparking.model.ParkingTime;
import com.nkcs.nkparking.service.ParkingTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingTimeServiceImpl implements ParkingTimeService {

    @Autowired
    private ParkingTimeMapper parkingTimeMapper;

    @Override
    public List<ParkingTime> queryTimeBySpaceId(Integer spaceId) {
        List<ParkingTime> parkingTimeList = parkingTimeMapper.selectBySpaceId(spaceId);
        for (ParkingTime parkingTime : parkingTimeList) {
            System.out.println(parkingTime.toString());
        }
        return  parkingTimeList;
    }
}
