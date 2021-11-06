package nk.parkar.user.service;

import nk.parkar.user.entity.Vehicle;
import com.baomidou.mybatisplus.extension.service.IService;
import nk.parkar.user.entity.vo.VehicleVo;

import java.util.List;

/**
 *
 * @author lihnagyu
 * @since 2021-11-06
 */
public interface VehicleService extends IService<Vehicle> {

    public boolean addVehicle(Vehicle vehicle);

    public boolean deleteVehicle(Vehicle vehicle);

    public List<String> getAllVehicleIdByUserId(String userId);
}
