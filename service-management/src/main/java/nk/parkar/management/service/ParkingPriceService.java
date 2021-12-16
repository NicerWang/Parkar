package nk.parkar.management.service;

import nk.parkar.management.model.ParkingPrice;

import java.util.List;
import java.util.Map;

public interface ParkingPriceService {
    List<ParkingPrice> getPriceDetail();
    int setPriceDetail(String key, Double value) ;

}
