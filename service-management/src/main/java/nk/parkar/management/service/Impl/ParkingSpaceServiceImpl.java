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

    @Autowired(required = false)
    private ParkingSpaceMapper parkingSpaceMapper;

    @Override
    public ParkingSpace querySpaceById(Integer spaceId) {
        return parkingSpaceMapper.selectByPrimaryKey(spaceId);
    }

    @Override
    public List<Integer> querySpaceByTime(Long startTime, Long endTime) {
        Map<String,Object> map = new HashMap<>();
        map.put("startTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(startTime*1000)));
        map.put("endTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(endTime*1000)));
        return parkingSpaceMapper.selectByAvailableTime(map);
    }

    @Override
    @Transactional
    public ParkingSpace updateSelective(ParkingSpace parkingSpace) {

        parkingSpaceMapper.updateByPrimaryKeySelective(parkingSpace);

        return parkingSpaceMapper.selectByPrimaryKey(parkingSpace.getSpaceId());
    }

    @Override
    public List<ParkingSpace> getAllSpaces() {
        return parkingSpaceMapper.selectAll();
    }

    @Override
    public List<ParkingSpace> getAllUnbannedSpaces() {
        return parkingSpaceMapper.selectUnbanned();
    }
}
