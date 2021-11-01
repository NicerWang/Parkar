package nk.parkar.management.service.Impl;

import nk.parkar.management.mapper.ParkingSpaceMapper;
import nk.parkar.management.model.ParkingSpace;
import nk.parkar.management.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    @Autowired
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
        List<Integer> spaceIdList = parkingSpaceMapper.selectByAvailableTime(map);
        return spaceIdList;
    }
}
