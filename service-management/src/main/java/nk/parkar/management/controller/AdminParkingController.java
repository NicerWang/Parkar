package nk.parkar.management.controller;

import nk.parkar.management.error.ControllerException.IllegalArgumentException;
import nk.parkar.management.model.ParkingOrder;
import nk.parkar.management.model.ParkingSpace;
import nk.parkar.management.model.ParkingTime;
import nk.parkar.management.service.ParkingBuilderService;
import nk.parkar.management.service.ParkingOrderService;
import nk.parkar.management.service.ParkingSpaceService;
import nk.parkar.management.service.ParkingTimeService;
import nk.parkar.management.util.CheckUtil;
import nk.parkar.management.util.JWTUtil;
import nk.parkar.management.util.entity.UnavailableTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class AdminParkingController {

    private ParkingSpaceService parkingSpaceService;
    private ParkingTimeService parkingTimeService;
    private ParkingOrderService parkingOrderService;
    private ParkingBuilderService parkingBuilderService;
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
    @Autowired
    public void setParkingBuilderService(ParkingBuilderService parkingBuilderService) {
        this.parkingBuilderService = parkingBuilderService;
    }


    /**
     * 查询指定车位的租赁模式、占用状态、禁用状态、占用时段
     * */
    @GetMapping("/administrator/parking/space/{spaceId}")
    public Map<String,Object> getSpaceInfoBySpaceId(@PathVariable("spaceId") String  spaceIdStr,
                                                    @RequestHeader("token") String token){
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
    public Map<String,Object> getAllOrderByPaidStat(@PathVariable("paidStat") String paidStr,
                                                    @RequestHeader("token") String token){
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/administrator/parking/order/list/{paidStat}");

        if(!JWTUtil.checkAdmin(token)){
            illegalArgumentException.addArgumentInfo("token");
            illegalArgumentException.addDescription("powerless token: "+token);
            throw illegalArgumentException;
        }

        if(!CheckUtil.checkPaidFormat(paidStr)){
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
                                                       String ban,
                                                       String booked,
                                                       String floor,
                                                       String xCoordinate,
                                                       String yCoordinate){
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/administrator/parking/order/list/{paidStat}");

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
        if(mode!=null){
            if(!CheckUtil.checkMode(illegalArgumentException,mode)){
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
        if(booked!=null){
            Byte isBooked;
            if(booked.equals("0")||booked.equals("1")){
                isBooked=Byte.parseByte(booked);
            }
            else{
                illegalArgumentException.addArgumentInfo("booked");
                illegalArgumentException.addDescription("illegal booked: "+booked);
                throw illegalArgumentException;
            }
            parkingSpace.setBooked(isBooked);
        }
        if(floor!=null){
            Integer floorNum;
            if(floor.equals("1")||floor.equals("2")||floor.equals("3")){
                floorNum=Integer.parseInt(floor);
            }
            else{
                illegalArgumentException.addArgumentInfo("floor");
                illegalArgumentException.addDescription("illegal floor: "+floor);
                throw illegalArgumentException;
            }
            parkingSpace.setFloor(floorNum);
        }
        if(xCoordinate!=null){
            if(!CheckUtil.checkNumberFormat(xCoordinate)){
                illegalArgumentException.addArgumentInfo("xCoordinate");
                illegalArgumentException.addDescription("illegal xCoordinate: "+xCoordinate);
                throw illegalArgumentException;
            }
            parkingSpace.setxCoordinate(Integer.parseInt(xCoordinate));
        }
        if(yCoordinate!=null){
            if(!CheckUtil.checkNumberFormat(yCoordinate)){
                illegalArgumentException.addArgumentInfo("yCoordinate");
                illegalArgumentException.addDescription("illegal yCoordinate: "+yCoordinate);
                throw illegalArgumentException;
            }
            parkingSpace.setyCoordinate(Integer.parseInt(yCoordinate));
        }
        ParkingSpace retSpace = parkingSpaceService.updateSelective(parkingSpace);
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("updated space",retSpace);
        return retMap;
    }


    private boolean checkSpaceIdValue(IllegalArgumentException illegalArgumentException,Integer spaceId){
        if (parkingSpaceService.querySpaceById(spaceId)==null){
            illegalArgumentException.addDescription("spaceId "+spaceId+" not found");
            illegalArgumentException.addArgumentInfo("spaceId");
            return false;
        }
        return true;
    }

    /**
     * 查询所有位置
     * */
    @GetMapping("/administrator/parking/space/list")
    public Map<String,Object> getAllSpaces(@RequestHeader("token") String token){
        if(!JWTUtil.checkAdmin(token)){
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/administrator/parking/space/list");
            illegalArgumentException.addArgumentInfo("token");
            illegalArgumentException.addDescription("powerless token: "+token);
            throw illegalArgumentException;
        }
        List<ParkingSpace> parkingSpaceList = parkingSpaceService.getAllSpaces();
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("spaceList",parkingSpaceList);
        return retMap;
    }

    @PostMapping("/administrator/build/space")
    public Map<String,Object> buildSpaces(@RequestHeader("token") String token){
        if(!JWTUtil.checkAdmin(token)){
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/administrator/parking/space/list");
            illegalArgumentException.addArgumentInfo("token");
            illegalArgumentException.addDescription("powerless token: "+token);
            throw illegalArgumentException;
        }
        //拿到已有最大ID
        Integer maxSpaceId = parkingBuilderService.getMaxSpaceId();
        //添加缺失的车位
        while(maxSpaceId<108){
            parkingBuilderService.insertNoneSpace();
            maxSpaceId++;
        }
        //分层


        //构建坐标
        for(int i=0;i<3;++i){
            for(int j=1;j<21;++j){
                Integer spaceId=36*i+j;
                Integer xCoordinate = 5+(j-1)*30;
                Integer yCoordinate = 5;
                parkingBuilderService.updateCoordinate(spaceId,xCoordinate,yCoordinate);
            }
            for(int j=21;j<37;++j){
                Integer spaceId = 36*i+j;
                Integer xCoordinate = 65+(j-21)*30;
                Integer yCoordinate = 95;
                parkingBuilderService.updateCoordinate(spaceId,xCoordinate,yCoordinate);
            }
        }

        Map<String,Object> retMap = new HashMap<>();
        retMap.put("finished",true);
        return retMap;
    }
}
