package nk.parkar.user.controller;


import nk.parkar.user.entity.Vehicle;
import nk.parkar.user.service.VehicleService;
import nk.parkar.user.utils.JWTUtil;
import nk.parkar.user.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public R addVehicle(@RequestBody Vehicle vehicle, HttpServletRequest request){
        vehicle.setUserId(JWTUtil.check(request.getHeader("token")));
        boolean isSuccess = vehicleService.addVehicle(vehicle);
        if(isSuccess){
            return R.ok().message("Add vehicle successful!");
        }
        return R.error().message("Add vehicle failed!");
    }

    @PostMapping("/deleteVehicle")
    public R deleteVehicle(@RequestBody Vehicle vehicle, HttpServletRequest request){
        vehicle.setUserId(JWTUtil.check(request.getHeader("token")));
        vehicleService.deleteVehicle(vehicle);
        return R.ok().message("Delete vehicle successful!");
    }

    @GetMapping("/getAllVehicleId/{userId}")
    public R getAllVehicleIdByUserId(@PathVariable String userId, HttpServletRequest request){
        String tokenResult = JWTUtil.check(request.getHeader("token"));
        if(tokenResult == null || tokenResult.length() == 0)
            return R.error().message("Invalid token");
        List<String> allVehicleIds = vehicleService.getAllVehicleIdByUserId(userId);
        return R.ok().message("Query successful!").data("allVehicleIds",allVehicleIds);
    }
}

