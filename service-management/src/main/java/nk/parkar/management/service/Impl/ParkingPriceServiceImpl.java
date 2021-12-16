package nk.parkar.management.service.Impl;

import nk.parkar.management.mapper.ParkingPriceMapper;
import nk.parkar.management.model.ParkingPrice;
import nk.parkar.management.service.ParkingPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParkingPriceServiceImpl implements ParkingPriceService {
    private ParkingPriceMapper parkingPriceMapper;

    @Autowired
    public void setParkingPriceMapper(ParkingPriceMapper parkingPriceMapper) {
        this.parkingPriceMapper = parkingPriceMapper;
    }

    @Override
    public List<ParkingPrice> getPriceDetail() {
        return parkingPriceMapper.getPriceDetail();
    }

    @Override
    public int setPriceDetail(String key, Double value) {
        ParkingPrice p = new ParkingPrice();
        p.setKey(key);
        p.setValue(value);
        return parkingPriceMapper.setPriceDetail(p);
    }
}
