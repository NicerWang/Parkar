package nk.parkar.management.controller;

import io.swagger.models.auth.In;
import nk.parkar.management.error.ControllerException.IllegalArgumentException;
import nk.parkar.management.model.ParkingOrder;
import nk.parkar.management.model.ParkingSpace;
import nk.parkar.management.model.ParkingTime;
import nk.parkar.management.service.ParkingOrderService;
import nk.parkar.management.service.ParkingSpaceService;
import nk.parkar.management.service.ParkingTimeService;
import nk.parkar.management.util.JWTUtil;
import nk.parkar.management.util.entity.UnavailableTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class AdminParkingController {

    private ParkingSpaceService parkingSpaceService;
    private ParkingTimeService parkingTimeService;
    private ParkingOrderService parkingOrderService;

    @Autowired
    public void setParkingOrderService(ParkingOrderService parkingOrderService) {
        this.parkingOrderService = parkingOrderService;
    }

    @Autowired
    public void setParkingSpaceService(ParkingSpaceService parkingSpaceService) {
        this.parkingSpaceService = parkingSpaceService;
    }

    @Autowired
    public void setParkingTimeService(ParkingTimeService parkingTimeService) {
        this.parkingTimeService = parkingTimeService;
    }

    private void tokenCheck(IllegalArgumentException illegalArgumentException, String token) {
        if (!JWTUtil.checkAdmin(token)) {
            illegalArgumentException.addDescription("Invalid token");
            throw illegalArgumentException;
        }
    }

    /**
     * 查询指定车位的所有信息以及占用时段
     */
    @GetMapping("/admin/space/{spaceId}")
    public Map<String, Object> getSpaceInfoBySpaceId(@PathVariable("spaceId") Integer spaceId,
                                                     @RequestHeader("token") String token) {
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/admin/space/{spaceId}");
        tokenCheck(illegalArgumentException, token);

        Map<String, Object> retMap = new HashMap<>();
        List<UnavailableTime> unavailableTimeList = new ArrayList<>();

        ParkingSpace parkingSpace = parkingSpaceService.querySpaceById(spaceId);
        if (parkingSpace != null) {

            List<ParkingTime> parkingTimeList = parkingTimeService.queryTimeBySpaceId(spaceId);
            if (!parkingTimeList.isEmpty()) {
                long currentTime = new Date().getTime();
                boolean validGap = false;
                for (ParkingTime parkingTime : parkingTimeList) {
                    long tempStartTime = parkingTime.getStartTime().getTime();
                    long tempEndTime = parkingTime.getEndTime().getTime();
                    if (!validGap) {
                        if (tempEndTime > currentTime) {
                            unavailableTimeList.add(new UnavailableTime(Math.max(currentTime, tempStartTime), tempEndTime));
                            validGap = true;
                        }
                    } else {
                        unavailableTimeList.add(new UnavailableTime(tempStartTime, tempEndTime));
                    }
                }
            }
            retMap.put("unavailableTimeList", unavailableTimeList);
            if(!unavailableTimeList.isEmpty()){
                parkingSpace.setBooked(true);
            }
            retMap.put("spaceInfo", parkingSpace);
        } else {
            illegalArgumentException.addDescription("SpaceId not found");
            throw illegalArgumentException;
        }
        return retMap;
    }


    /**
     * 查询所有订单
     */
    @GetMapping("/admin/order")
    public Map<String, Object> getAllOrder(@RequestHeader("token") String token,
                                           @RequestParam Integer page,
                                           @RequestParam Integer pageSize,
                                           String userId,
                                           String licenseNumber,
                                           Integer mode,
                                           Integer paid,
                                           Integer space_id) {
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/admin/order");
        tokenCheck(illegalArgumentException, token);
        int start = (page - 1) * pageSize;
        Map<String,Object> map = new HashMap<>();
        if(userId != null) map.put("userId",userId);
        if(licenseNumber != null) map.put("licenseNumber","%" + licenseNumber + "%");
        if(mode != null) map.put("mode",mode);
        if(paid != null) map.put("paid",paid);
        if(space_id != null) map.put("spaceId",space_id);
        List<ParkingOrder> parkingOrderList = parkingOrderService.queryCondition(map,start,pageSize);
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("orderList", parkingOrderList);
        return retMap;
    }


    /**
     * 更新车位的信息，更新时必须提供所有原有信息
     */
    @PutMapping("/admin/space/{spaceId}")
    public Boolean updateSpaceInfoBySpaceId(@PathVariable("spaceId") Integer spaceId,
                                            @RequestHeader("token") String token,
                                            @RequestBody ParkingSpace parkingSpace) {
        parkingSpace.setSpaceId(spaceId);
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/admin/space/{spaceId}");
        tokenCheck(illegalArgumentException, token);
        if(parkingSpace.getMode() < 0 || parkingSpace.getMode() > 3){
            illegalArgumentException.addDescription("Illegal Mode.");
            throw illegalArgumentException;
        }
        if (!parkingSpaceService.checkExist(spaceId)) {
            illegalArgumentException.addDescription("SpaceId " + spaceId + " not found.");
            throw illegalArgumentException;
        }
        parkingSpace.setBooked(false);
        return parkingSpaceService.update(parkingSpace) == 1;
    }


    /**
     * 查询所有车位
     */
    @GetMapping("/admin/space")
    public Map<String, Object> getAllSpaces(@RequestHeader("token") String token) {
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/admin/space");
        tokenCheck(illegalArgumentException, token);
        List<ParkingSpace> parkingSpaceList = parkingSpaceService.getAllSpaces();
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("spaceList", parkingSpaceList);
        return retMap;
    }

}
