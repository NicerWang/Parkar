package nk.parkar.management.service;


import nk.parkar.management.model.ParkingOrder;

import java.util.List;
import java.util.Map;

public interface ParkingOrderService {
    Map<String,Object> insertOrderFromUser(String userId, Integer spaceId, String mode, Long startTime, Long endTime);

    List<ParkingOrder> queryByUserId(String userId);

    List<ParkingOrder> queryAll();

    List<ParkingOrder> queryByPaidStat(Byte paid);
}
