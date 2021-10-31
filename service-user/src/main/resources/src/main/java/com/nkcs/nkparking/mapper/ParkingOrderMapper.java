package com.nkcs.nkparking.mapper;

import com.nkcs.nkparking.model.ParkingOrder;

public interface ParkingOrderMapper {
    int deleteByPrimaryKey(Integer orderId);

    int insert(ParkingOrder record);

    int insertSelective(ParkingOrder record);

    ParkingOrder selectByPrimaryKey(Integer orderId);

    int updateByPrimaryKeySelective(ParkingOrder record);

    int updateByPrimaryKey(ParkingOrder record);
}