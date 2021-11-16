package nk.parkar.management.service.Impl;

import nk.parkar.management.mapper.ParkingSpaceMapper;
import nk.parkar.management.model.ParkingSpace;
import nk.parkar.management.service.ParkingBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingBuilderServiceImpl implements ParkingBuilderService {

    @Autowired(required = false)
    ParkingSpaceMapper parkingSpaceMapper;

    @Override
    public void insertNoneSpace() {
        parkingSpaceMapper.insertNone();
    }

    @Override
    public Integer getMaxSpaceId() {
       return parkingSpaceMapper.selectMaxSpaceId();
    }

    @Override
    public void updateCoordinate(Integer spaceId, Integer xCoordinate, Integer yCoordinate) {
        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setSpaceId(spaceId);
        parkingSpace.setxCoordinate(xCoordinate);
        parkingSpace.setyCoordinate(yCoordinate);
        parkingSpaceMapper.updateByPrimaryKeySelective(parkingSpace);
    }
}
