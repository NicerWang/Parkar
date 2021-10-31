package com.nkcs.nkparking.mapper;

import com.nkcs.nkparking.model.ParkingTime;

import java.util.List;

public interface ParkingTimeMapper {
    int deleteByPrimaryKey(Integer timeId);

    int insert(ParkingTime record);

    int insertSelective(ParkingTime record);

    ParkingTime selectByPrimaryKey(Integer timeId);

    int updateByPrimaryKeySelective(ParkingTime record);

    int updateByPrimaryKey(ParkingTime record);

    List<ParkingTime> selectBySpaceId(Integer spaceId);
}