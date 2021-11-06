package nk.parkar.user.controller;


import nk.parkar.user.entity.Vehicle;
import nk.parkar.user.entity.vo.VehicleVo;
import nk.parkar.user.service.VehicleService;
import nk.parkar.user.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author lihnagyu
 * @since 2021-11-06
 */
@RestController
@RequestMapping("/")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/addVehicle")
    public R addVehicle(@RequestBody Vehicle vehicle){
        boolean isSuccess = vehicleService.addVehicle(vehicle);
        if(isSuccess){
            return R.ok().message("Add vehicle successful!");
        }
        return R.ok().message("Add vehicle failed!");
    }

    @PostMapping("/deleteVehicle")
    public R deleteVehicle(@RequestBody Vehicle vehicle){
        vehicleService.deleteVehicle(vehicle);
        return R.ok().message("Delete vehicle successful!");
    }

    @GetMapping("/getAllVehicleId/{userId}")
    public R getAllVehicleIdByUserId(@PathVariable String userId){
        List<VehicleVo> allVehicleIds = vehicleService.getAllVehicleIdByUserId(userId);
        return R.ok().message("Query successful!").data("allVehicleIds",allVehicleIds);
    }
}

