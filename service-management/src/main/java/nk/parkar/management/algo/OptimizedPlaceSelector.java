package nk.parkar.management.algo;

import nk.parkar.management.model.ParkingSpace;
import nk.parkar.management.service.ParkingSpaceService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OptimizedPlaceSelector {

    public Integer select(ParkingSpaceService parkingSpaceService, Long startTime, Long endTime){
        List<Integer> spaceIdList = parkingSpaceService.querySpaceByTime(startTime, endTime);
        List<ParkingSpace> allSpaces = parkingSpaceService.getAllSpaces();
        Map<Integer, Integer> costs = new HashMap<>();
        for (ParkingSpace allSpace : allSpaces) {
            int cost = allSpace.getFloor() * 100 + allSpace.getxCoordinate();
            costs.put(allSpace.getSpaceId(), cost);
        }
        for(Integer i:spaceIdList){
            if( costs.get(i - 1) != null){
                costs.put(i - 1, costs.get(i - 1) - 50);
            }
            if( costs.get(i + 1) != null){
                costs.put(i + 1, costs.get(i + 1) - 50);
            }
        }
        int ret = 1;
        int min = 100000;
        for (Integer integer : spaceIdList) {
            if (costs.get(integer) < min) {
                min = costs.get(integer);
                ret = integer;
            }
        }
        return ret;
    }
}
