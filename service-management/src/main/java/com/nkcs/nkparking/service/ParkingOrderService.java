package com.nkcs.nkparking.service;


import com.nkcs.nkparking.model.ParkingOrder;

import java.util.Map;

public interface ParkingOrderService {
    Map<String,Object> insertOrderFromUser(Integer userId, Integer spaceId, String mode, Long startTime, Long endTime);

}
