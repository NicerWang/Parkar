package nk.parkar.management.controller;

import nk.parkar.management.error.ControllerException.IllegalArgumentException;
import nk.parkar.management.model.ParkingOrder;
import nk.parkar.management.model.ParkingTime;
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
public class UserParkingController {

    private ParkingTimeService parkingTimeService;
    private ParkingSpaceService parkingSpaceService;
    private ParkingOrderService parkingOrderService;


    @Autowired
    public void setParkingTimeService(ParkingTimeService parkingTimeService) {
        this.parkingTimeService = parkingTimeService;
    }
    @Autowired
    public void setParkingSpaceService(ParkingSpaceService parkingSpaceService) {
        this.parkingSpaceService = parkingSpaceService;
    }
    @Autowired
    public void setParkingOrderService(ParkingOrderService parkingOrderService) {
        this.parkingOrderService = parkingOrderService;
    }

    /**
     * 查询指定时间段的可用车位id
     *
     * retMap{
     *      availableSpaceIdList:[Integer...]
     *      startTime:Long
     *      endTime:Long
     *      }
    */
    @GetMapping("/order/{mode}/space/{startTime}/{endTime}")
    public Object getSpaceByAvailableTime(@PathVariable("mode") String mode,
                                          @PathVariable("startTime") String startTimeStr,
                                          @PathVariable("endTime") String endTimeStr,
                                          @RequestHeader("token") String token){
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/order/{mode}/space/{startTime}/{endTime}");

        if(JWTUtil.check(token)==null){
            illegalArgumentException.addDescription("invalid token: "+token);
            illegalArgumentException.addArgumentInfo("token");
            throw illegalArgumentException;
        }

        //判断mode是否合法
        CheckUtil.checkMode(illegalArgumentException,mode);
        //判断startTime endTime是否形式合法
        CheckUtil.checkTimeFormat(illegalArgumentException,startTimeStr,endTimeStr);
        if(!illegalArgumentException.getArgumentInfoList().isEmpty()){
            throw illegalArgumentException;
        }

        //判断time数值是否合法
        Long startTime = startTimeStr.length()==13?Long.parseLong(startTimeStr.substring(0,10)):Long.parseLong(startTimeStr);
        Long endTime = endTimeStr.length()==13?Long.parseLong(endTimeStr.substring(0,10)):Long.parseLong(endTimeStr);
        if(startTime.compareTo(endTime)>=0){
            illegalArgumentException.addDescription("illegal time:"+startTimeStr+"|"+endTimeStr+" ==> numerical problem");
            illegalArgumentException.addArgumentInfo("startTime|endTime");
            throw illegalArgumentException;
        }

        List<Integer> spaceIdList = parkingSpaceService.querySpaceByTime(startTime, endTime);
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("startTime",startTime);
        retMap.put("endTime",endTime);
        retMap.put("availableSpaceIdList",spaceIdList);
        return retMap;
    }


    /**
     * 查询指定车位的可用时间段
     *
     * retMap{
     *     availableAnytime:boolean,
     *     mode:Integer,
     *     spaceId:Integer,
     *     unavailableTimeList:[UnavailableTime...]
     * }
     * */
    @GetMapping("/order/{mode}/time/{spaceId}")
    public Object getUnavailableTimeBySpaceId(@PathVariable("spaceId") String spaceIdStr,
                                              @PathVariable("mode") String mode,
                                              @RequestHeader("token") String token){
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/order/{mode}/time/{spaceId}");

        if(JWTUtil.check(token)==null){
            illegalArgumentException.addDescription("invalid token: "+token);
            illegalArgumentException.addArgumentInfo("token");
            throw illegalArgumentException;
        }

        //判断mode是否合法
        CheckUtil.checkMode(illegalArgumentException,mode);

        //判断spaceId形式是否合法
        CheckUtil.checkSpaceIdFormat(illegalArgumentException,spaceIdStr);

        if(!illegalArgumentException.getArgumentInfoList().isEmpty())
            throw illegalArgumentException;

        //判断spaceId数值是否合法
        Integer spaceId = Integer.parseInt(spaceIdStr);
        if(!checkSpaceIdValue(illegalArgumentException,spaceId))
            throw illegalArgumentException;

        Map<String,Object> retMap = new HashMap<>();
        retMap.put("spaceId",spaceId);

        //查询已有不可用时段
        List<ParkingTime> parkingTimeList = parkingTimeService.queryTimeBySpaceId(spaceId);
        List<ParkingTime> middleTimeList = new ArrayList<>();
        //获取当前时间
        long currentTime = new Date().getTime();
        //对不可用时段进行初步筛选
        for(ParkingTime parkingTime:parkingTimeList){
            if (parkingTime.getEndTime().getTime()>currentTime){
                middleTimeList.add(parkingTime);
            }
        }
        //无影响后需预约的不可用时段
        if(middleTimeList.isEmpty()){
            retMap.put("availableAnytime",true);
            retMap.put("mode",mode);
            retMap.put("unavailableTimeList:",null);
        }
        //存在影响后需预约的不可用时段
        else{
            retMap.put("availableAnytime",false);
            retMap.put("mode",mode);
            List<UnavailableTime> unavailableTimeList = new ArrayList<>();
            String pattern = "yyyy--MM-dd HH:mm:ss";
            Long minDis = 0L;
            //根据租赁模式设置最小可用间隔 及 返回的时间格式
            switch (mode) {
                case "year":
                    pattern = "yyyy-MM-dd";
                    minDis = Long.parseLong("31536000000");
                    break;
                case "month":
                    pattern = "yyyy-MM-dd";
                    minDis = Long.parseLong("2592000000");
                    break;
                case "day":
                    pattern = "yyyy-MM-dd HH:mm";
                    minDis = Long.parseLong("3600000");
                    break;
            }

            //筛选不可用时间
            //首个不可用时段进行特殊处理
            if(middleTimeList.get(0).getStartTime().getTime()<=currentTime||middleTimeList.get(0).getStartTime().getTime()-currentTime<=minDis){
                unavailableTimeList.add(new UnavailableTime(new SimpleDateFormat(pattern).format(new Date(currentTime)),
                                                            new SimpleDateFormat(pattern).format(middleTimeList.get(0).getEndTime())));
            }
            else{
                unavailableTimeList.add(new UnavailableTime(new SimpleDateFormat(pattern).format(middleTimeList.get(0).getStartTime()),
                                                            new SimpleDateFormat(pattern).format(middleTimeList.get(0).getEndTime())));
            }
            //后续可不用时段 判断是否合并
            for(int i=1;i<middleTimeList.size();++i){
                Date tempStartTime = middleTimeList.get(i).getStartTime();
                Date tempEndTime = middleTimeList.get(i).getEndTime();
                Long currentDis = tempStartTime.getTime()-middleTimeList.get(i-1).getEndTime().getTime();
                //与前一个不可用时段合并
                if(currentDis.compareTo(minDis)<0){
                    unavailableTimeList.get(unavailableTimeList.size()-1).
                            setEndTime(new SimpleDateFormat(pattern).format(tempEndTime));
                }
                //不合并，作为新的不可用时段加入结果
                else{
                    unavailableTimeList.add(new UnavailableTime(new SimpleDateFormat(pattern).format(tempStartTime),
                                                                new SimpleDateFormat(pattern).format(tempEndTime)));
                }
            }
            retMap.put("unavailableTimeList",unavailableTimeList);
        }
        return retMap;
    }


    /**
     * 提交用户预约
     *
     * retMap{
     *     newTime:ParkingTime,
     *     newOrder:ParkingOrder
     * }
     * */
    @PostMapping("/order/{mode}/{spaceId}/{startTime}/{endTime}")
    public Object addOrderFromUser(@PathVariable("mode") String mode,
                                   @PathVariable("spaceId") String spaceIdStr,
                                   @PathVariable("startTime") String startTimeStr,
                                   @PathVariable("endTime") String endTimeStr,
                                   @RequestHeader("token") String token){
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/order/{mode}/{spaceId}/{startTime}/{endTime}");
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
        long currentTime = new Date().getTime();
        Long startTime = startTimeStr.length()==13?Long.parseLong(startTimeStr.substring(0,10)):Long.parseLong(startTimeStr);
        Long endTime = endTimeStr.length()==13?Long.parseLong(endTimeStr.substring(0,10)):Long.parseLong(endTimeStr);
        if(startTime.compareTo(endTime)>=0||startTime.compareTo(currentTime/1000)<0){
            illegalArgumentException.addDescription("illegal time:"+startTimeStr+"|"+endTimeStr+" ==> numerical problem");
            illegalArgumentException.addArgumentInfo("startTime|endTime");
        }
        if(!illegalArgumentException.getArgumentInfoList().isEmpty()){
            throw illegalArgumentException;
        }
        return parkingOrderService.insertOrderFromUser(userId, spaceId, mode, startTime, endTime);
    }


    /**
    * 查询用户所有预约
     *
     * retMap{
     *     orderList:[ParkingOrder...]
     * }
    * */
    @GetMapping("/order")
    public Object getOrderListByUserId(@RequestHeader("token") String token){
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/order/{userId}");
        String userId=JWTUtil.check(token);
        if(userId==null){
            illegalArgumentException.addDescription("invalid token: "+token);
            illegalArgumentException.addArgumentInfo("token");
            throw illegalArgumentException;
        }

        if(CheckUtil.checkUserId(userId)){
            List<ParkingOrder> parkingOrderList = parkingOrderService.queryByUserId(userId);
            Map<String ,Object> retMap = new HashMap<>();
            retMap.put("orderList",parkingOrderList);
            return retMap;
        }
        else{
            illegalArgumentException.addArgumentInfo("userId");
            illegalArgumentException.addDescription("illegal userId: "+userId);
            throw illegalArgumentException;
        }
    }


    @PutMapping("/order/cancel/{orderId}")
    public Map<String,Object> cancelOrder(@PathVariable("orderId")Integer orderId,@RequestHeader("token") String token){
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/order/cancel/{orderId}");

        if(JWTUtil.check(token)==null){
            illegalArgumentException.addDescription("invalid token: "+token);
            illegalArgumentException.addArgumentInfo("token");
            throw illegalArgumentException;
        }
        ParkingOrder parkingOrder = parkingOrderService.queryByOrderId(orderId);
        if(parkingOrder==null){
            illegalArgumentException.addDescription("no such order in table parking_order by orderId: "+orderId);
            illegalArgumentException.addArgumentInfo("orderId");
            throw illegalArgumentException;
        }
        if((parkingOrder.getStartTime().getTime()-(new Date().getTime())<1800)){
            illegalArgumentException.addArgumentInfo("orderId");
            illegalArgumentException.addDescription("orders can only be cancelled 30 minutes prior to the effective time");
            throw illegalArgumentException;
        }
        ParkingTime parkingTime = new ParkingTime();
        parkingTime.setSpaceId(parkingOrder.getSpaceId());
        parkingTime.setStartTime(parkingOrder.getStartTime());
        parkingTime.setEndTime(parkingOrder.getEndTime());
        parkingOrderService.cancelOrder(orderId,parkingTime);
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("cancelOrder",parkingOrder);
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


}
