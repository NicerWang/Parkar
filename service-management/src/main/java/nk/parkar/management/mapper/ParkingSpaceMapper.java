package nk.parkar.management.mapper;

import nk.parkar.management.model.ParkingSpace;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ParkingSpaceMapper {
    int selectMaxSpaceId();

    List<Integer> selectByAvailableTime(Map<String,Object> paramList);

    ParkingSpace selectById(Integer spaceId);

    int deleteById(Integer spaceId);

    int insert(ParkingSpace record);

    int updateById(ParkingSpace record);

    int updateByIdSelective(ParkingSpace record);

    List<ParkingSpace> selectAll();
}