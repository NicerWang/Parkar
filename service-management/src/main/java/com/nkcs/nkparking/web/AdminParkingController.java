package com.nkcs.nkparking.web;

import com.nkcs.nkparking.error.ControllerException.IllegalArgumentException;
import com.nkcs.nkparking.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminParkingController {

    @Autowired
    private ParkingSpaceService parkingSpaceService;

    /**
     * 查询指定车位的租赁模式、占用状态、禁用状态、占用时段
     * */
    @GetMapping("/administrator/parking/space/{spaceId}")
    public Object getSpaceBySpaceId(@PathVariable("spaceId") Integer spaceId){
        Object space = parkingSpaceService.querySpaceById(spaceId);
        System.out.println("from /administrator/parking/space/{spaceId}   space:");
        if(space!=null)
            System.out.println(space.toString());
        else{
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException("/administrator/parking/space/{spaceId}");
            illegalArgumentException.addDescription("spaceId:"+spaceId+" not found");
            illegalArgumentException.addArgumentInfo("{spaceId}");
        }
        return space;
    }
}
