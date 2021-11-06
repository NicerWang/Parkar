package nk.parkar.management.controller;


import nk.parkar.management.model.ParkingOrder;
import nk.parkar.management.service.ParkingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MachineParkingController {
    private ParkingOrderService parkingOrderService;
    @Autowired
    public void setParkingOrderService(ParkingOrderService parkingOrderService) {
        this.parkingOrderService = parkingOrderService;
    }


    @GetMapping("/machine/elevator/space/{licenseNumber}")
    public Map<String,Object> getSpaceIdByLicenseNumber(@PathVariable("licenseNumber")String licenseNumber){
        Map<String,Object> retMap = new HashMap<>();
        ParkingOrder parkingOrder = parkingOrderService.querySpaceIdByLicenseNumber(licenseNumber);
        Boolean hasOrder= parkingOrder != null;
        if(hasOrder){
            retMap.put("spaceId",parkingOrder.getSpaceId());
            retMap.put("startTime",parkingOrder.getStartTime());
            retMap.put("endTime",parkingOrder.getEndTime());
        }
        else{
            retMap.put("spaceId",null);
            retMap.put("startTime",null);
            retMap.put("endTime",null);
        }
        retMap.put("hasOrder",hasOrder);

        return retMap;
    }
}
