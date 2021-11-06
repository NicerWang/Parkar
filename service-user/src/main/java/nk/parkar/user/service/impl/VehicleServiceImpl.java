package nk.parkar.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import nk.parkar.user.entity.Vehicle;
import nk.parkar.user.entity.vo.VehicleVo;
import nk.parkar.user.mapper.VehicleMapper;
import nk.parkar.user.service.VehicleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lihnagyu
 * @since 2021-11-06
 */
@Service
public class VehicleServiceImpl extends ServiceImpl<VehicleMapper, Vehicle> implements VehicleService {

    @Override
    public boolean addVehicle(Vehicle vehicle) {
        QueryWrapper<Vehicle> wrapper = new QueryWrapper<>();
        wrapper.eq("vehicle_id",vehicle.getVehicleId())
                .eq("user_id",vehicle.getUserId());
        Integer count = baseMapper.selectCount(wrapper);
        if(count != 0){
            return false;
        }
        baseMapper.insert(vehicle);
        return true;
    }

    @Override
    public boolean deleteVehicle(Vehicle vehicle) {
        baseMapper.deleteById(vehicle.getVehicleId());
        return true;
    }

    @Override
    public List<VehicleVo> getAllVehicleIdByUserId(String userId) {
        QueryWrapper<Vehicle> wrapper = new QueryWrapper<>();
        wrapper.select("vehicle_id").eq("user_id",userId);
        List<Vehicle> vehicles = baseMapper.selectList(wrapper);
        ArrayList<VehicleVo> vehicleVos = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            VehicleVo vehicleVo = new VehicleVo();
            vehicleVo.setVehicleId(vehicle.getVehicleId());
            vehicleVos.add(vehicleVo);
        }
        return vehicleVos;
    }
}
