package nk.parkar.management.mapper;

import nk.parkar.management.model.ParkingTime;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ParkingTimeMapper {
    List<ParkingTime> selectBySpaceId(Integer spaceId);

    int deleteById(Integer timeId);

    int insert(ParkingTime record);

    int deleteWithoutId(ParkingTime record);
}