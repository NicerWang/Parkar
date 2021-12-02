package nk.parkar.management.service.Impl;

import nk.parkar.management.mapper.ParkingSpaceMapper;
import nk.parkar.management.model.ParkingSpace;
import nk.parkar.management.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    private ParkingSpaceMapper parkingSpaceMapper;

    @Autowired
    public void setParkingSpaceMapper(ParkingSpaceMapper parkingSpaceMapper) {
        this.parkingSpaceMapper = parkingSpaceMapper;
    }

    @Override
    public ParkingSpace querySpaceById(Integer spaceId) {
        return parkingSpaceMapper.selectById(spaceId);
    }

    @Override
    public List<Integer> querySpaceByTime(Long startTime, Long endTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("startTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(startTime)));
        map.put("endTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(endTime)));
        return parkingSpaceMapper.selectByAvailableTime(map);
    }

    @Override
    @Transactional
    public Integer update(ParkingSpace parkingSpace) {
        return parkingSpaceMapper.updateById(parkingSpace);
    }

    @Override
    @Transactional
    public Integer updateSelective(ParkingSpace parkingSpace) {
        return parkingSpaceMapper.updateByIdSelective(parkingSpace);
    }

    @Override
    public List<ParkingSpace> getAllSpaces() {
        return parkingSpaceMapper.selectAll();
    }

    @Override
    public boolean checkExist(Integer spaceId) {
        return this.querySpaceById(spaceId) != null;
    }
}
