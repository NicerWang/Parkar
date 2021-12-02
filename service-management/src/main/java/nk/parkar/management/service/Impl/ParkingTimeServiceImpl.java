package nk.parkar.management.service.Impl;

import nk.parkar.management.mapper.ParkingTimeMapper;
import nk.parkar.management.model.ParkingTime;
import nk.parkar.management.service.ParkingTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingTimeServiceImpl implements ParkingTimeService {

    private ParkingTimeMapper parkingTimeMapper;

    @Autowired
    public void setParkingTimeMapper(ParkingTimeMapper parkingTimeMapper) {
        this.parkingTimeMapper = parkingTimeMapper;
    }
    
    @Override
    public List<ParkingTime> queryTimeBySpaceId(Integer spaceId) {
        return parkingTimeMapper.selectBySpaceId(spaceId);
    }
}
