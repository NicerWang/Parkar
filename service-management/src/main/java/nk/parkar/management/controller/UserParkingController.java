package nk.parkar.management.controller;

import nk.parkar.management.algo.OptimizedPlaceSelector;
import nk.parkar.management.error.ControllerException.IllegalArgumentException;
import nk.parkar.management.model.ParkingOrder;
import nk.parkar.management.model.ParkingSpace;
import nk.parkar.management.model.ParkingTime;
import nk.parkar.management.service.ParkingOrderService;
import nk.parkar.management.service.ParkingSpaceService;
import nk.parkar.management.util.CheckUtil;
import nk.parkar.management.util.JWTUtil;
import nk.parkar.management.util.PriceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserParkingController {

    private ParkingSpaceService parkingSpaceService;
    private ParkingOrderService parkingOrderService;
    private OptimizedPlaceSelector optimizedPlaceSelector;

    @Autowired
    public void setParkingSpaceService(ParkingSpaceService parkingSpaceService) {
        this.parkingSpaceService = parkingSpaceService;
    }

    @Autowired
    public void setParkingOrderService(ParkingOrderService parkingOrderService) {
        this.parkingOrderService = parkingOrderService;
    }

    @Autowired
    public void setOptimizedPlaceSelector(OptimizedPlaceSelector optimizedPlaceSelector) {
        this.optimizedPlaceSelector = optimizedPlaceSelector;
    }

    private String tokenCheck(IllegalArgumentException illegalArgumentException, String token) {
        String checkResult = JWTUtil.check(token);
        if (checkResult == null) {
            illegalArgumentException.addDescription("Invalid token");
            throw illegalArgumentException;
        }
        return checkResult;
    }

    private void timeCheck(IllegalArgumentException illegalArgumentException, long start, long end) {
        String timeCheck = CheckUtil.checkTime(start,end);
        if(timeCheck != null){
            illegalArgumentException.addDescription(timeCheck);
            throw illegalArgumentException;
        }
    }

    /**
     * 查询停车价格
     */
    @GetMapping("/order/price")
    public Double getPrice(@RequestParam Integer mode, Long startTime, Long endTime) {
        if(mode == 0 && ( startTime == null || endTime == null)){
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/order/price");
            illegalArgumentException.addDescription("mode 0 need start and end time");
            throw illegalArgumentException;
        }
        return PriceUtil.getPrice(mode, startTime, endTime);
    }

    /**
     * 指定时间段，据此查询可用车位id
     */
    @GetMapping("/order/space")
    public Object getSpaceByAvailableTime(@RequestParam Long startTime,
                                          @RequestParam Long endTime,
                                          @RequestHeader("token") String token) {
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/order/space");
        tokenCheck(illegalArgumentException, token);
        timeCheck(illegalArgumentException,startTime,endTime);

        List<Integer> spaceIdList = parkingSpaceService.querySpaceByTime(startTime, endTime);
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("availableSpaceIdList", spaceIdList);
        return retMap;
    }

    /**
     * 提交用户预约订单
     */
    @PostMapping("/order/submit")
    public Object addOrderFromUser(@RequestParam Integer mode,
                                   @RequestParam Integer spaceId,
                                   @RequestParam Long startTime,
                                   @RequestParam Long endTime,
                                   @RequestParam String licenseNumber,
                                   @RequestHeader("token") String token) {
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/order/submit");
        String userId = tokenCheck(illegalArgumentException,token);
        timeCheck(illegalArgumentException,startTime,endTime);
        if (!parkingSpaceService.checkExist(spaceId)) {
            illegalArgumentException.addDescription("SpaceId " + spaceId + " not found.");
            throw illegalArgumentException;
        }
        if(mode < 0 || mode > 3){
            illegalArgumentException.addDescription("Illegal Mode.");
            throw illegalArgumentException;
        }
        if ((startTime - (new Date().getTime()) < 1800000)) {
            illegalArgumentException.addDescription("Reserve time must start after 30 minutes.");
            throw illegalArgumentException;
        }
        if (startTime % 1800000 != 0 || endTime % 1800000 != 0) {
            illegalArgumentException.addDescription("Reserve time must at one or half hour.");
            throw illegalArgumentException;
        }
        if (endTime - startTime < 1800000) {
            illegalArgumentException.addDescription("Reservation at least 30 minutes.");
            throw illegalArgumentException;
        }


        return parkingOrderService.insertOrder(userId, licenseNumber, spaceId, mode, startTime, endTime);
    }


    /**
     * 查询用户所有订单
     */
    @GetMapping("/order")
    public Object getOrderListByUserId(@RequestHeader("token") String token) {
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/order/{userId}");
        String userId = tokenCheck(illegalArgumentException,token);

        List<ParkingOrder> parkingOrderList = parkingOrderService.queryByUserId(userId);
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("orderList", parkingOrderList);
        return retMap;
    }


    /**
     * 查询所有车位信息
     */
    @GetMapping("/order/space/list")
    public Map<String, Object> getAllSpaces(@RequestHeader("token") String token) {
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/order/space/list");
        tokenCheck(illegalArgumentException, token);

        List<ParkingSpace> parkingSpaceList = parkingSpaceService.getAllSpaces();
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("spaceList", parkingSpaceList);
        return retMap;
    }

    /**
     * 取消订单
     */
    @PutMapping("/order/cancel/{orderId}")
    public Boolean cancelOrder(@PathVariable("orderId") Integer orderId, @RequestHeader("token") String token) {
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/order/cancel/{orderId}");

        String userId = tokenCheck(illegalArgumentException,token);

        ParkingOrder parkingOrder = parkingOrderService.queryByOrderId(orderId);
        if (parkingOrder == null) {
            illegalArgumentException.addDescription("Order does not exist");
            throw illegalArgumentException;
        }
        if(!parkingOrder.getUserId().equals(userId)){
            illegalArgumentException.addDescription("UserId does not match");
            throw illegalArgumentException;
        }

        if ((parkingOrder.getStartTime().getTime() - (new Date().getTime()) < 1800000)) {
            illegalArgumentException.addDescription("Order can only be cancelled 30 minutes before start");
            throw illegalArgumentException;
        }
        ParkingTime parkingTime = new ParkingTime();
        parkingTime.setSpaceId(parkingOrder.getSpaceId());
        parkingTime.setStartTime(parkingOrder.getStartTime());
        parkingTime.setEndTime(parkingOrder.getEndTime());
        parkingOrderService.cancelOrder(orderId, parkingTime);
        return true;
    }

    @GetMapping("/order/space/rec")
    Integer getOptimizedPlace(@RequestParam Long startTime, @RequestParam Long endTime, @RequestHeader("token") String token){
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/order/space/rec");

        tokenCheck(illegalArgumentException,token);

        return optimizedPlaceSelector.select(parkingSpaceService, startTime, endTime);
    }
}
