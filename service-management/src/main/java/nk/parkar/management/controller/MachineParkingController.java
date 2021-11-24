package nk.parkar.management.controller;


import nk.parkar.management.error.ControllerException.IllegalArgumentException;
import nk.parkar.management.model.ParkingOrder;
import nk.parkar.management.model.ParkingSpace;
import nk.parkar.management.service.ParkingOrderService;
import nk.parkar.management.service.ParkingSpaceService;
import nk.parkar.management.util.CheckUtil;
import nk.parkar.management.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/machine/elevator/space/{licenseNumber}")
    public Map<String,Object> getSpaceInfoByLicenseNumber(@PathVariable("licenseNumber")String licenseNumber){
        Map<String,Object> retMap = new HashMap<>();
        Map<String,Object> orderMap = parkingOrderService.querySpaceIdByLicenseNumber(licenseNumber);
        Boolean hasOrder = orderMap != null;
        if(hasOrder){
            Boolean hasExpired = (Boolean) orderMap.get("hasExpired");
            ParkingOrder currentOrder = (ParkingOrder) orderMap.get("firstOrder");
            ParkingOrder laterOrder = (ParkingOrder) orderMap.get("secondOrder");
            retMap.put("hasExpired",hasExpired);
            ParkingSpace parkingSpace = null;
            //当前未过期
            if(!hasExpired) {
                parkingSpace = parkingSpaceService.querySpaceById(currentOrder.getSpaceId());
            }
            //当前已过期，返回后续订单车位
            else if(laterOrder != null){
                parkingSpace = parkingSpaceService.querySpaceById(laterOrder.getSpaceId());
            }
            retMap.put("space",parkingSpace);
            retMap.put("currentOrder",currentOrder);
            retMap.put("laterOrder",laterOrder);
        }
        else{
            retMap.put("hasExpired",false);
            retMap.put("space",null);
            retMap.put("currentOrder",null);
            retMap.put("laterOrder",null);
        }
        retMap.put("hasOrder",hasOrder);
        return retMap;
    }

    @PutMapping("/machine/sensor/space/{spaceId}")
    public Map<String,Object> updateSpaceInfoBySpaceId(@PathVariable("spaceId")String spaceIdStr,
                                                       @RequestHeader("token") String token,
                                                       String occupied,
                                                       String ban){
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/machine/sensor/space/{spaceId}");

        if(!JWTUtil.checkAdmin(token)){
            illegalArgumentException.addArgumentInfo("token");
            illegalArgumentException.addDescription("powerless token: "+token);
            throw illegalArgumentException;
        }

        CheckUtil.checkSpaceIdFormat(illegalArgumentException,spaceIdStr);
        if(!illegalArgumentException.getArgumentInfoList().isEmpty()){
            throw illegalArgumentException;
        }

        Integer spaceId = Integer.parseInt(spaceIdStr);
        ParkingSpace parkingSpace = new ParkingSpace();
        if(!checkSpaceIdValue(illegalArgumentException,spaceId)){
            illegalArgumentException.addDescription("spaceId "+spaceId+" not found");
            illegalArgumentException.addArgumentInfo("spaceId");
            throw illegalArgumentException;
        }
        else{
            parkingSpace.setSpaceId(spaceId);
        }

        if(occupied!=null){
            if(!CheckUtil.checkOccupied(illegalArgumentException,occupied)){
                throw illegalArgumentException;
            }
            Byte isOccupied=Byte.parseByte(occupied);
            parkingSpace.setOccupied(isOccupied);
        }
        if(ban!=null){
            Byte isBan;
            if(ban.equals("0")||ban.equals("1")){
                isBan=Byte.parseByte(ban);
            }
            else{
                illegalArgumentException.addArgumentInfo("ban");
                illegalArgumentException.addDescription("illegal ban: "+ban);
                throw illegalArgumentException;
            }
            parkingSpace.setBan(isBan);
        }
        ParkingSpace retSpace = parkingSpaceService.updateSelective(parkingSpace);
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("updated space",retSpace);
        return retMap;
    }

    @PostMapping("/machine/elevator/order/{mode}/{spaceId}/{startTime}/{endTime}/{licenseNumber}")
    public Object addOrderFromElevator(@PathVariable("mode") String mode,
                                   @PathVariable("spaceId") String spaceIdStr,
                                   @PathVariable("startTime") String startTimeStr,
                                   @PathVariable("endTime") String endTimeStr,
                                   @PathVariable("licenseNumber") String licenseNumber,
                                   @RequestHeader("token") String token){
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/machine/elevator/order/{mode}/{spaceId}/{startTime}/{endTime}/{licenseNumber}");
        String userId=JWTUtil.check(token);
        if(userId==null){
            illegalArgumentException.addDescription("invalid token: "+token);
            illegalArgumentException.addArgumentInfo("token");
            throw illegalArgumentException;
        }

        //判断mode是否合法
        CheckUtil.checkMode(illegalArgumentException,mode);
        //判断userId形式是否合法
        if(!CheckUtil.checkUserId(userId)){
            illegalArgumentException.addDescription("illegal userId");
            illegalArgumentException.addArgumentInfo("userId");
        }
        //判断spaceId形式是否合法
        CheckUtil.checkSpaceIdFormat(illegalArgumentException,spaceIdStr);
        //判断time形式是否合法
        CheckUtil.checkTimeFormat(illegalArgumentException,startTimeStr,endTimeStr);
        if(!illegalArgumentException.getArgumentInfoList().isEmpty()){
            throw illegalArgumentException;
        }

        //判断spaceId数值是否合法
        Integer spaceId = Integer.parseInt(spaceIdStr);
        checkSpaceIdValue(illegalArgumentException,spaceId);

        //判断time数值是否合法
        Long startTime = startTimeStr.length()==13?Long.parseLong(startTimeStr.substring(0,10)):Long.parseLong(startTimeStr);
        Long endTime = endTimeStr.length()==13?Long.parseLong(endTimeStr.substring(0,10)):Long.parseLong(endTimeStr);
        if(startTime.compareTo(endTime)>=0){
            illegalArgumentException.addDescription("illegal time:"+startTimeStr+"|"+endTimeStr+" ==> numerical problem");
            illegalArgumentException.addArgumentInfo("startTime|endTime");
        }
        if(!illegalArgumentException.getArgumentInfoList().isEmpty()){
            throw illegalArgumentException;
        }
        return parkingOrderService.insertOrder(userId, licenseNumber,spaceId, mode, startTime, endTime);
    }




    private boolean checkSpaceIdValue(IllegalArgumentException illegalArgumentException,Integer spaceId){
        if (parkingSpaceService.querySpaceById(spaceId)==null){
            illegalArgumentException.addDescription("spaceId "+spaceId+" not found");
            illegalArgumentException.addArgumentInfo("spaceId");
            return false;
        }
        return true;
    }

}
