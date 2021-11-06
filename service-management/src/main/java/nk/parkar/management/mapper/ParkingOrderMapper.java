package nk.parkar.management.mapper;

import nk.parkar.management.model.ParkingOrder;

import java.util.List;


public interface ParkingOrderMapper {
    int deleteByPrimaryKey(Integer orderId);

    int insert(ParkingOrder record);

    int insertSelective(ParkingOrder record);

    ParkingOrder selectByPrimaryKey(Integer orderId);

    List<ParkingOrder> selectByUserId(String userId);

    List<ParkingOrder> selectAll();

    int updateByPrimaryKeySelective(ParkingOrder record);

    int updateByPrimaryKey(ParkingOrder record);

    List<ParkingOrder> selectByPaidStat(Byte paid);

    List<ParkingOrder> selectByLicenseNumber(String licenseNumber);
}