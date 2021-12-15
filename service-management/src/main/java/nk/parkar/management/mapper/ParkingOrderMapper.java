package nk.parkar.management.mapper;

import nk.parkar.management.model.ParkingOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ParkingOrderMapper {
    ParkingOrder selectById(Integer orderId);

    List<ParkingOrder> selectAll();

    List<ParkingOrder> selectByLicenseNumber(String licenseNumber);

    List<ParkingOrder> selectCondition(Map<String,Object> map);

    int deleteById(Integer orderId);

    int insert(ParkingOrder record);

    int updateById(ParkingOrder record);
}