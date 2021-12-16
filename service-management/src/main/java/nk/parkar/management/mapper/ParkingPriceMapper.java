package nk.parkar.management.mapper;

import nk.parkar.management.model.ParkingPrice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ParkingPriceMapper {
    List<ParkingPrice> getPriceDetail();
    int setPriceDetail(ParkingPrice price);
}
