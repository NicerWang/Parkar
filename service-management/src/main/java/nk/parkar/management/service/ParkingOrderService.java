package nk.parkar.management.service;


import java.util.Map;

public interface ParkingOrderService {
    Map<String,Object> insertOrderFromUser(Integer userId, Integer spaceId, String mode, Long startTime, Long endTime);

}
