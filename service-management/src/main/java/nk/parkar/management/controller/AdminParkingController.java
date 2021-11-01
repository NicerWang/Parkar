package nk.parkar.management.controller;

import nk.parkar.management.error.ControllerException.IllegalArgumentException;
import nk.parkar.management.model.ParkingSpace;
import nk.parkar.management.model.ParkingTime;
import nk.parkar.management.service.ParkingSpaceService;
import nk.parkar.management.service.ParkingTimeService;
import nk.parkar.management.util.entity.UnavailableTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.regex.Pattern;

@RestController
public class AdminParkingController {

    @Autowired
    private ParkingSpaceService parkingSpaceService;
    @Autowired
    private ParkingTimeService parkingTimeService;

    /**
     * 查询指定车位的租赁模式、占用状态、禁用状态、占用时段
     * */
    @GetMapping("/administrator/parking/space/{spaceId}")
    public Map<String,Object> getSpaceBySpaceId(@PathVariable("spaceId") String  spaceIdStr){
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/administrator/parking/space/{spaceId}");
        checkUserIdFormat(illegalArgumentException,spaceIdStr);
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
                for(;indexUsefulInfo<parkingTimeList.size();++indexUsefulInfo){
                    if(parkingTimeList.get(indexUsefulInfo).getEndTime().getTime()>currentTime){

                        break;
                    }
                }
            }
        }
        else{
            illegalArgumentException.addDescription("spaceId:"+spaceId+" not found");
            illegalArgumentException.addArgumentInfo("{spaceId}");
            throw illegalArgumentException;
        }
        return retMap;
    }




    private boolean checkUserIdFormat(IllegalArgumentException illegalArgumentException, String userIdStr) {
        String pattern = "^\\d+$";
        if(!Pattern.matches(pattern,userIdStr)){
            illegalArgumentException.addDescription("illegal userId:"+userIdStr);
            illegalArgumentException.addArgumentInfo("userId");
            return false;
        }
        return true;
    }
}
