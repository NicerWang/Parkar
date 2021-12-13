package nk.parkar.management.service.Impl;

import nk.parkar.management.error.ControllerException.TransactionException;
import nk.parkar.management.mapper.ParkingOrderMapper;
import nk.parkar.management.mapper.ParkingTimeMapper;
import nk.parkar.management.model.ParkingOrder;
import nk.parkar.management.model.ParkingTime;
import nk.parkar.management.service.ParkingOrderService;
import nk.parkar.management.util.PriceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ParkingOrderServiceImpl implements ParkingOrderService {

    ParkingOrderMapper parkingOrderMapper;

    ParkingTimeMapper parkingTimeMapper;

    @Autowired
    public void setParkingOrderMapper(ParkingOrderMapper parkingOrderMapper) {
        this.parkingOrderMapper = parkingOrderMapper;
    }

    @Autowired
    public void setParkingTimeMapper(ParkingTimeMapper parkingTimeMapper) {
        this.parkingTimeMapper = parkingTimeMapper;
    }


    @Override
    @Transactional
    public Boolean insertOrder(String userId, String licenseNumber, Integer spaceId, Integer mode, Long startTime, Long endTime) {
        TransactionException transactionException = new TransactionException("insertOrder");

        // 保证订单对应车位和时间是空闲的
        List<ParkingTime> parkingTimeList = parkingTimeMapper.selectBySpaceId(spaceId);
        if (!parkingTimeList.isEmpty()) {
            for (ParkingTime parkingTime : parkingTimeList) {
                Long tempStartTime = parkingTime.getStartTime().getTime();
                Long tempEndTime = parkingTime.getEndTime().getTime();
                if ((startTime.compareTo(tempStartTime) < 0 && endTime.compareTo(tempStartTime) > 0) ||
                        (startTime.compareTo(tempStartTime) >= 0 && startTime.compareTo(tempEndTime) < 0)) {
                    transactionException.addDescription("Sorry, The time period in the space has been occupied.");
                    throw transactionException;
                }
            }
        }

        // 保证当前车辆在当前时间内只有一个车位的预约
        List<ParkingOrder> parkingOrderListByLicense = parkingOrderMapper.selectByLicenseNumber(licenseNumber);
        int licenseIndex = parkingOrderListByLicense.size() - 1;
        while (licenseIndex >= 0) {
            Long tempStartTime = parkingOrderListByLicense.get(licenseIndex).getStartTime().getTime();
            Long tempEndTime = parkingOrderListByLicense.get(licenseIndex).getEndTime().getTime();
            if ((startTime.compareTo(tempStartTime) < 0 && endTime.compareTo(tempStartTime) > 0) ||
                    (startTime.compareTo(tempStartTime) >= 0 && startTime.compareTo(tempEndTime) < 0)) {
                transactionException.addDescription("This License Number (" + licenseNumber + ") already has a reservation of overlap time!");
                throw transactionException;
            }
            licenseIndex--;
        }

        ParkingOrder parkingOrder = new ParkingOrder();
        parkingOrder.setMode(mode);
        parkingOrder.setPaid(false);
        parkingOrder.setEndTime(new Date(endTime));
        parkingOrder.setStartTime(new Date(startTime));
        parkingOrder.setUserId(userId);
        parkingOrder.setPrice(PriceUtil.getPrice(mode,startTime,endTime));
        parkingOrder.setSpaceId(spaceId);
        parkingOrder.setLicenseNumber(licenseNumber);
        parkingOrderMapper.insert(parkingOrder);

        ParkingTime parkingTime = new ParkingTime();
        parkingTime.setEndTime(new Date(endTime));
        parkingTime.setStartTime(new Date(startTime));
        parkingTime.setSpaceId(spaceId);
        parkingTimeMapper.insert(parkingTime);
        return true;
    }

    @Override
    public List<ParkingOrder> queryByUserId(String userId) {
        return parkingOrderMapper.selectByUserId(userId);
    }

    @Override
    public List<ParkingOrder> queryAll() {
        return parkingOrderMapper.selectAll();
    }

    @Override
    public List<ParkingOrder> queryByPaidStat(Boolean paid) {
        return parkingOrderMapper.selectByPaidStat(paid);
    }

    @Override
    public ParkingOrder queryByOrderId(Integer orderId) {
        return parkingOrderMapper.selectById(orderId);
    }

    @Override
    @Transactional
    public Boolean extendOrder(ParkingOrder order,  ParkingTime parkingTime) {
        TransactionException transactionException = new TransactionException("extendOrder");
        // 保证订单对应车位和时间是空闲的
        List<ParkingTime> parkingTimeList = parkingTimeMapper.selectBySpaceId(parkingTime.getSpaceId());
        Long startTime = parkingTime.getStartTime().getTime();
        Long endTime = parkingTime.getEndTime().getTime();
        if (!parkingTimeList.isEmpty()) {
            for (ParkingTime pt : parkingTimeList) {
                Long tempStartTime = pt.getStartTime().getTime();
                Long tempEndTime = pt.getEndTime().getTime();
                if ((startTime.compareTo(tempStartTime) < 0 && endTime.compareTo(tempStartTime) > 0) ||
                        (startTime.compareTo(tempStartTime) >= 0 && startTime.compareTo(tempEndTime) < 0)) {
                    transactionException.addDescription("Sorry, The time period in the space has been occupied.");
                    throw transactionException;
                }
            }
        }

        // 保证当前车辆在当前时间内只有一个车位的预约
        List<ParkingOrder> parkingOrderListByLicense = parkingOrderMapper.selectByLicenseNumber(order.getLicenseNumber());
        int licenseIndex = parkingOrderListByLicense.size() - 1;
        while (licenseIndex >= 0) {
            Long tempStartTime = parkingOrderListByLicense.get(licenseIndex).getStartTime().getTime();
            Long tempEndTime = parkingOrderListByLicense.get(licenseIndex).getEndTime().getTime();
            if ((startTime.compareTo(tempStartTime) < 0 && endTime.compareTo(tempStartTime) > 0) ||
                    (startTime.compareTo(tempStartTime) >= 0 && startTime.compareTo(tempEndTime) < 0)) {
                transactionException.addDescription("This License Number (" + order.getLicenseNumber() + ") already has a reservation of overlap time! Cancel before extend.");
                throw transactionException;
            }
            licenseIndex--;
        }
        ParkingTime oldParkingTime = new ParkingTime();
        oldParkingTime.setSpaceId(order.getSpaceId());
        oldParkingTime.setStartTime(order.getStartTime());
        oldParkingTime.setEndTime(order.getEndTime());

        parkingTimeMapper.deleteWithoutId(oldParkingTime);

        ParkingTime newParkingTime = new ParkingTime();
        newParkingTime.setSpaceId(order.getSpaceId());
        newParkingTime.setStartTime(order.getStartTime());
        newParkingTime.setEndTime(parkingTime.getEndTime());
        order.setEndTime(parkingTime.getEndTime());
        order.setPrice(PriceUtil.getPrice(0,order.getStartTime().getTime(), order.getEndTime().getTime()));
        parkingTimeMapper.insert(newParkingTime);

        return parkingOrderMapper.updateById(order) == 1;
    }

    @Override
    @Transactional
    public void cancelOrder(Integer orderId, ParkingTime parkingTime) {
        TransactionException transactionException = new TransactionException("extendOrder");
        if (parkingTimeMapper.deleteWithoutId(parkingTime) < 1) {
            transactionException.addDescription("[Internal Error] Time interval remove failed.");
            throw transactionException;
        }
        parkingOrderMapper.deleteById(orderId);
    }

    @Override
    public ParkingOrder querySpaceIdByLicenseNumber(String licenseNumber) {
        List<ParkingOrder> parkingOrderList = parkingOrderMapper.selectByLicenseNumber(licenseNumber);
        if (parkingOrderList.isEmpty())
            return null;
        long now = new Date().getTime();
        for(int i = 0; i < parkingOrderList.size(); i++){
            if(now >= parkingOrderList.get(i).getStartTime().getTime() && now <= parkingOrderList.get(i).getEndTime().getTime()){
                return parkingOrderList.get(i);
            }
        }
        return null;
    }


}
