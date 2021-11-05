package nk.parkar.management.controller;

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

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

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



    /**
     * 查询指定车位的租赁模式、占用状态、禁用状态、占用时段
     * */
    @GetMapping("/administrator/parking/space/{spaceId}")
    public Map<String,Object> getSpaceInfoBySpaceId(@PathVariable("spaceId") String  spaceIdStr,@RequestHeader("token") String token){
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/administrator/parking/space/{spaceId}");

        if(!JWTUtil.checkAdmin(token)){
            illegalArgumentException.addArgumentInfo("token");
            illegalArgumentException.addDescription("powerless token: "+token);
            throw illegalArgumentException;
        }

        Integer spaceId = Integer.parseInt(spaceIdStr);
        Map<String,Object> retMap = new HashMap<>();
        List<UnavailableTime> unavailableTimeList = new ArrayList<>();

        if(!illegalArgumentException.getArgumentInfoList().isEmpty()){
            throw illegalArgumentException;
        }

        ParkingSpace parkingSpace = parkingSpaceService.querySpaceById(spaceId);
        if(parkingSpace!=null){
            retMap.put("spaceInfo",parkingSpace);
            List<ParkingTime> parkingTimeList = parkingTimeService.queryTimeBySpaceId(spaceId);
            if(!parkingTimeList.isEmpty()){
                long currentTime = new Date().getTime();
                int indexUsefulInfo=0;
                boolean markForAdd = false;
                String pattern = "yyyy--MM-dd HH:mm:ss";
                for(;indexUsefulInfo<parkingTimeList.size();++indexUsefulInfo){
                    long tempStartTime = parkingTimeList.get(indexUsefulInfo).getStartTime().getTime();
                    long tempEndTime = parkingTimeList.get(indexUsefulInfo).getEndTime().getTime();
                    if(!markForAdd){
                        if(tempEndTime>currentTime){
                            if(tempStartTime<currentTime){
                                unavailableTimeList.add(new UnavailableTime(new SimpleDateFormat(pattern).format(currentTime),
                                        new SimpleDateFormat(pattern).format(tempEndTime)));
                            }
                            else{
                                unavailableTimeList.add(new UnavailableTime(new SimpleDateFormat(pattern).format(tempStartTime),
                                        new SimpleDateFormat(pattern).format(tempEndTime)));
                            }
                            markForAdd=true;
                        }
                    }
                    else{
                        unavailableTimeList.add(new UnavailableTime(new SimpleDateFormat(pattern).format(tempStartTime),
                                new SimpleDateFormat(pattern).format(tempEndTime)));
                    }
                }
            }
            retMap.put("unavailableTimeList",unavailableTimeList);
        }
        else{
            illegalArgumentException.addDescription("spaceId:"+spaceId+" not found");
            illegalArgumentException.addArgumentInfo("{spaceId}");
            throw illegalArgumentException;
        }
        return retMap;
    }


    /**
     * 查询所有订单 仅管理员可用
     * */
    @GetMapping("/administrator/parking/order/list")
    public Map<String,Object> getAllOrder(@RequestHeader("token") String token){
        if(!JWTUtil.checkAdmin(token)){
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/administrator/parking/order/list");
            illegalArgumentException.addArgumentInfo("token");
            illegalArgumentException.addDescription("powerless token: "+token);
            throw illegalArgumentException;
        }
        List<ParkingOrder> parkingOrderList = parkingOrderService.queryAll();
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("orderList",parkingOrderList);
        return retMap;
    }

    @GetMapping("/administrator/parking/order/list/{paidStat}")
    public Map<String,Object> getAllOrderByPaidStat(@PathVariable("paidStat") String paidStr,@RequestHeader("token") String token){
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/administrator/parking/order/list/{paidStat}");

        if(!JWTUtil.checkAdmin(token)){
            illegalArgumentException.addArgumentInfo("token");
            illegalArgumentException.addDescription("powerless token: "+token);
            throw illegalArgumentException;
        }

        if(!checkPaidFormat(paidStr)){
            illegalArgumentException.addArgumentInfo("paidStat");
            illegalArgumentException.addDescription("illegal paidStat: "+paidStr+"      available value:0 | 1");
            throw illegalArgumentException;
        }
        Byte paid = Byte.parseByte(paidStr);
        List<ParkingOrder> parkingOrderList = parkingOrderService.queryByPaidStat(paid);
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("orderList",parkingOrderList);
        return retMap;
    }

    @PutMapping("/administrator/parking/space/{spaceId}")
    public Map<String,Object> updateSpaceInfoBySpaceId(@PathVariable("spaceId")String spaceIdStr,
                                                       @RequestHeader("token") String token,
                                                       String occupied,
                                                       String mode,
                                                       String ban){
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/administrator/parking/order/list/{paidStat}");

        if(!JWTUtil.checkAdmin(token)){
            illegalArgumentException.addArgumentInfo("token");
            illegalArgumentException.addDescription("powerless token: "+token);
            throw illegalArgumentException;
        }

        checkSpaceIdFormat(illegalArgumentException,spaceIdStr);
        if(!illegalArgumentException.getArgumentInfoList().isEmpty()){
            throw illegalArgumentException;
        }

        Integer spaceId = Integer.parseInt(spaceIdStr);
        ParkingSpace parkingSpace = new ParkingSpace();
        if(!checkSpaceIdValue(illegalArgumentException,spaceId)){
            throw illegalArgumentException;
        }
        else{
            parkingSpace.setSpaceId(spaceId);
        }

        if(occupied!=null){
            if(!checkOccupied(illegalArgumentException,occupied)){
                throw illegalArgumentException;
            }
            Byte isOccupied=Byte.parseByte(occupied);
            parkingSpace.setOccupied(isOccupied);
        }
        if(mode!=null){
            if(!checkMode(illegalArgumentException,mode)){
                throw illegalArgumentException;
            }
            Integer modeCode=null;

            switch (mode) {
                case "year":
                   modeCode=2;
                    break;
                case "month":
                    modeCode=1;
                    break;
                case "day":
                    modeCode=0;
                    break;
            }
            parkingSpace.setMode(modeCode);
        }
        if(ban!=null){
            Byte isBan;
            if(ban.equals("0")){
                isBan=Byte.parseByte("0");
            }
            else if(ban.equals("1")){
                isBan=Byte.parseByte("1");
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





    private boolean checkPaidFormat(String paidStr){
        return paidStr.equals("0") || paidStr.equals("1");
    }

    private boolean checkUserId(String userIdStr) {

        return true;
    }
    private void checkSpaceIdFormat(IllegalArgumentException illegalArgumentException, String spaceIdStr){
        String pattern = "^\\d+$";
        if(!Pattern.matches(pattern,spaceIdStr)){
            illegalArgumentException.addDescription("illegal spaceId:"+spaceIdStr);
            illegalArgumentException.addArgumentInfo("spaceId");
        }
    }

    private boolean checkSpaceIdValue(IllegalArgumentException illegalArgumentException,Integer spaceId){
        if (parkingSpaceService.querySpaceById(spaceId)==null){
            illegalArgumentException.addDescription("spaceId "+spaceId+" not found");
            illegalArgumentException.addArgumentInfo("spaceId");
            return false;
        }
        return true;
    }

    private boolean checkOccupied(IllegalArgumentException illegalArgumentException,String occupiedStr){
        if(occupiedStr.equals("0")||occupiedStr.equals("1")){
            return true;
        }
        else{
            illegalArgumentException.addDescription("illegal occupied: "+occupiedStr);
            illegalArgumentException.addArgumentInfo("occupied");
            return false;
        }
    }

    private boolean checkMode(IllegalArgumentException illegalArgumentException, String mode){
        if(!mode.equals("day")&&!mode.equals("month")&&!mode.equals("year")){
            illegalArgumentException.addDescription("illegal mode:"+mode);
            illegalArgumentException.addArgumentInfo("mode");
            return false;
        }
        else{
            return true;
        }
    }
}
