package nk.parkar.management.service;


import nk.parkar.management.model.ParkingOrder;
import nk.parkar.management.model.ParkingTime;

import java.util.List;
import java.util.Map;

public interface ParkingOrderService {
    Boolean insertOrder(String userId, String licenseNumber, Integer spaceId, Integer mode, Long startTime, Long endTime);

    ParkingOrder queryByOrderId(Integer orderId);

    List<ParkingOrder> queryByUserId(String userId, Integer start, Integer length);

    void cancelOrder(Integer orderId, ParkingTime parkingTime);

    public Boolean extendOrder(ParkingOrder order, ParkingTime parkingTime);

    ParkingOrder querySpaceIdByLicenseNumber(String licenseNumber);

    public List<ParkingOrder> queryCondition(Map<String,Object> map, Integer start, Integer length);
}
