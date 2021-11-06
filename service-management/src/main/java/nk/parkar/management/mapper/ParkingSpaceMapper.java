package nk.parkar.management.mapper;

import nk.parkar.management.model.ParkingSpace;

import java.util.List;
import java.util.Map;

public interface ParkingSpaceMapper {
    int deleteByPrimaryKey(Integer spaceId);

    int insert(ParkingSpace record);

    int insertSelective(ParkingSpace record);

    ParkingSpace selectByPrimaryKey(Integer spaceId);

    int updateByPrimaryKeySelective(ParkingSpace record);

    int updateByPrimaryKey(ParkingSpace record);

    List<Integer> selectByAvailableTime(Map<String,Object> paramList);

    List<ParkingSpace> selectAll();
}