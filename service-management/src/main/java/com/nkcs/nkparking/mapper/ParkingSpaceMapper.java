package com.nkcs.nkparking.mapper;

import com.nkcs.nkparking.model.ParkingSpace;

import java.util.List;
import java.util.Map;

public interface ParkingSpaceMapper {
    int deleteByPrimaryKey(Integer spaceId);

    int insert(ParkingSpace record);

    int insertSelective(ParkingSpace record);

    ParkingSpace selectByPrimaryKey(Integer spaceId);

    int updateByPrimaryKeySelective(ParkingSpace record);

    int updateByPrimaryKey(ParkingSpace record);

    List<Integer> selectByAvailableTime(Map<String,Object> paramList);
}