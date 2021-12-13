package nk.parkar.management.controller;


import nk.parkar.management.error.ControllerException.IllegalArgumentException;
import nk.parkar.management.error.ControllerException.TransactionException;
import nk.parkar.management.model.ParkingOrder;
import nk.parkar.management.model.ParkingSpace;
import nk.parkar.management.service.ParkingOrderService;
import nk.parkar.management.service.ParkingSpaceService;
import nk.parkar.management.util.CheckUtil;
import nk.parkar.management.util.JWTUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MachineParkingController {
    private ParkingOrderService parkingOrderService;
    private ParkingSpaceService parkingSpaceService;

    @Autowired
    public void setParkingSpaceService(ParkingSpaceService parkingSpaceService) {
        this.parkingSpaceService = parkingSpaceService;
    }

    @Autowired
    public void setParkingOrderService(ParkingOrderService parkingOrderService) {
        this.parkingOrderService = parkingOrderService;
    }

    /**
     * 入场时查询有无订单
     */
    @GetMapping("/machine/camera/{licenseNumber}")
    public Map<String, Object> getSpaceInfoByLicenseNumber(@PathVariable("licenseNumber") String licenseNumber) {
        Map<String, Object> retMap = new HashMap<>();
        ParkingOrder currentOrder = parkingOrderService.querySpaceIdByLicenseNumber(licenseNumber);
        Boolean hasOrder = currentOrder != null;
        if (hasOrder) {
            ParkingSpace parkingSpace = parkingSpaceService.querySpaceById(currentOrder.getSpaceId());
            retMap.put("space", parkingSpace);
            retMap.put("currentOrder", currentOrder);
        } else {
            retMap.put("space", null);
            retMap.put("currentOrder", null);
        }
        retMap.put("hasOrder", hasOrder);
        return retMap;
    }

    /**
     * 修改传感器状态
     */
    @PutMapping("/machine/sensor/{spaceId}")
    public Boolean updateSpaceInfoBySpaceId(@PathVariable("spaceId") Integer spaceId,
                                                        @RequestParam Boolean occupied) {
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/machine/sensor/{spaceId}");

        ParkingSpace parkingSpace = new ParkingSpace();
        if (!parkingSpaceService.checkExist(spaceId)) {
            throw illegalArgumentException;
        } else {
            parkingSpace.setSpaceId(spaceId);
        }
        parkingSpace.setOccupied(occupied);
        return parkingSpaceService.updateSelective(parkingSpace) == 1;
    }

    /**
     * 根据支付状态查询订单
     * 所有用于传递的时间戳，必须都是13位的
     */
    @PostMapping("/machine/order")
    public Object addOrderFromElevator(@RequestParam String licenseNumber,
                                       @RequestParam Integer spaceId,
                                       @RequestParam Long endTime,
                                       @RequestHeader("token") String token) {

        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/machine/order");
        String userId = JWTUtil.check(token);
        Long startTime = new Date().getTime();

        String timeCheck = CheckUtil.checkTime(startTime,endTime);
        endTime -= 1000;
        if(timeCheck != null){
            illegalArgumentException.addDescription(timeCheck);
            throw illegalArgumentException;
        }
        if (!CheckUtil.checkUserId(userId)) {
            illegalArgumentException.addDescription("Illegal userId");
            throw illegalArgumentException;
        }
        if (!parkingSpaceService.checkExist(spaceId)) {
            illegalArgumentException.addDescription("SpaceId " + spaceId + " not found.");
            throw illegalArgumentException;
        }
        if (endTime % 1800000 != 0) {
            illegalArgumentException.addDescription("End time must at one or half hour.");
            throw illegalArgumentException;
        }
        if (endTime - startTime < 1800000) {
            illegalArgumentException.addDescription("Reservation at least 30 minutes.");
            throw illegalArgumentException;
        }
        return parkingOrderService.insertOrder(userId, licenseNumber, spaceId, 0, startTime, endTime);
    }
}
