package nk.parkar.management.service;


import nk.parkar.management.model.ParkingOrder;
import nk.parkar.management.model.ParkingTime;

import java.util.List;
import java.util.Map;

public interface ParkingOrderService {
    Boolean insertOrder(String userId, String licenseNumber, Integer spaceId, Integer mode, Long startTime, Long endTime);

    List<ParkingOrder> queryByUserId(String userId);

    List<ParkingOrder> queryAll();

    List<ParkingOrder> queryByPaidStat(Boolean paid);

    ParkingOrder queryByOrderId(Integer orderId);

    void cancelOrder(Integer orderId, ParkingTime parkingTime);

    ParkingOrder querySpaceIdByLicenseNumber(String licenseNumber);
}
