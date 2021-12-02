package nk.parkar.management.mapper;

import nk.parkar.management.model.ParkingOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ParkingOrderMapper {
    ParkingOrder selectById(Integer orderId);

    List<ParkingOrder> selectByUserId(String userId);

    List<ParkingOrder> selectByPaidStat(Boolean paid);

    List<ParkingOrder> selectAll();

    List<ParkingOrder> selectByLicenseNumber(String licenseNumber);

    int deleteById(Integer orderId);

    int insert(ParkingOrder record);

    int updateById(ParkingOrder record);
}