package nk.parkar.management.service.Impl;

import nk.parkar.management.error.ControllerException.TransactionException;
import nk.parkar.management.mapper.ParkingOrderMapper;
import nk.parkar.management.mapper.ParkingTimeMapper;
import nk.parkar.management.model.ParkingOrder;
import nk.parkar.management.model.ParkingTime;
import nk.parkar.management.service.ParkingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ParkingOrderServiceImpl implements ParkingOrderService {

    @Autowired(required = false)
    ParkingOrderMapper parkingOrderMapper;
    @Autowired(required = false)
    ParkingTimeMapper parkingTimeMapper;

    @Override
    @Transactional
    public Map<String,Object> insertOrder(String userId, String licenseNumber, Integer spaceId, String mode, Long startTime, Long endTime) {

        TransactionException transactionException = new TransactionException("insertOrder");
        List<ParkingTime> parkingTimeList = parkingTimeMapper.selectBySpaceId(spaceId);
        if(!parkingTimeList.isEmpty()){
            for(ParkingTime parkingTime:parkingTimeList){
                Long tempStartTime = parkingTime.getStartTime().getTime()/1000;
                Long tempEndTime = parkingTime.getEndTime().getTime()/1000;

//                if((tempStartTime.compareTo(endTime)<0&&tempStartTime.compareTo(startTime)>0)||
//                        (tempEndTime.compareTo(endTime)<0&&tempEndTime.compareTo(startTime)>0)||
//                        (tempEndTime.compareTo(endTime)==0&&tempStartTime.compareTo(startTime)==0)){
                if((startTime.compareTo(tempStartTime)<0&&endTime.compareTo(tempStartTime)>0)||
                        (startTime.compareTo(tempStartTime)>=0&&startTime.compareTo(tempEndTime)<0)){
                    transactionException.addDescription("the time period in the space has been occupied");
                    throw transactionException;
                }
            }
        }

        List<ParkingOrder> parkingOrderListByLicense = parkingOrderMapper.selectByLicenseNumber(licenseNumber);
        int licenseIndex = parkingOrderListByLicense.size()-1;
        while(licenseIndex>=0){
            Long tempStartTime = parkingOrderListByLicense.get(licenseIndex).getStartTime().getTime()/1000;
            Long tempEndTime = parkingOrderListByLicense.get(licenseIndex).getEndTime().getTime()/1000;
            if((startTime.compareTo(tempStartTime)<0&&endTime.compareTo(tempStartTime)>0)||
                    (startTime.compareTo(tempStartTime)>=0&&startTime.compareTo(tempEndTime)<0)){
                transactionException.addDescription("Multiple parking spaces cannot be reserved at the same time with the same license plate number!");
                throw transactionException;
            }

            licenseIndex--;
        }

        BigDecimal unitPrice = new BigDecimal("0");
        int modeCode = 0;
        BigDecimal totalPrice;
        long tempTimeUnits = 0L;
        long dis = endTime-startTime;
        switch (mode) {
            case "day":
                unitPrice = new BigDecimal("4.0");
                tempTimeUnits = dis - 3600 * (dis / 3600) > 60 ? dis / 3600 + 1 : dis / 3600;
                break;
            case "month":
                unitPrice = new BigDecimal("1.5");
                modeCode = 1;
                tempTimeUnits = dis / 3600;
                break;
            case "year":
                unitPrice = new BigDecimal("1.2");
                modeCode = 2;
                tempTimeUnits = dis / 3600;
                break;
        }
        BigDecimal timeUnits = new BigDecimal(tempTimeUnits);
        totalPrice = timeUnits.multiply(unitPrice);

        ParkingOrder parkingOrder = new ParkingOrder();
        parkingOrder.setMode(modeCode);
        parkingOrder.setPaid((byte) 0);
        parkingOrder.setEndTime(new Date(endTime*1000));
        parkingOrder.setStartTime(new Date(startTime*1000));
        parkingOrder.setUserId(userId);
        parkingOrder.setPrice(totalPrice);
        parkingOrder.setSpaceId(spaceId);
        parkingOrder.setLicenseNumber(licenseNumber);
        parkingOrderMapper.insertSelective(parkingOrder);

        ParkingTime parkingTime = new ParkingTime();
        parkingTime.setEndTime(new Date(endTime*1000));
        parkingTime.setStartTime(new Date(startTime*1000));
        parkingTime.setSpaceId(spaceId);
        parkingTimeMapper.insertSelective(parkingTime);

        Integer orderResult = parkingOrder.getOrderId();
        Integer timeResult = parkingTime.getTimeId();

        ParkingOrder newOrder = parkingOrderMapper.selectByPrimaryKey(orderResult);
        ParkingTime newTime = parkingTimeMapper.selectByPrimaryKey(timeResult);

        Map<String,Object> retMap = new HashMap<>();
        retMap.put("newTime",newTime);
        retMap.put("newOrder",newOrder);
        return retMap;
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
    public List<ParkingOrder> queryByPaidStat(Byte paid) {
        return parkingOrderMapper.selectByPaidStat(paid);
    }

    @Override
    public ParkingOrder queryByOrderId(Integer orderId) {
        return parkingOrderMapper.selectByPrimaryKey(orderId);
    }

    @Override
    @Transactional
    public void cancelOrder(Integer orderId, ParkingTime parkingTime) {
        TransactionException transactionException = new TransactionException("cancelOrder");
        if(parkingTimeMapper.deleteByNoPrimaryKey(parkingTime)<1){
            transactionException.addDescription("no such period in table parking_time");
            throw transactionException;
       }
       parkingOrderMapper.deleteByPrimaryKey(orderId);
    }

    @Override
    public Map<String,Object> querySpaceIdByLicenseNumber(String licenseNumber) {
        List<ParkingOrder> parkingOrderList = parkingOrderMapper.selectByLicenseNumber(licenseNumber);
        Map<String,Object> retMap = new HashMap<>();
        if(parkingOrderList.isEmpty())
            return null;
        int retIndex=0;
        boolean hasExpired = false;
        while(retIndex<parkingOrderList.size()){
            if((new Date().getTime())-parkingOrderList.get(retIndex).getStartTime().getTime()>1800000){
                if((new Date().getTime())>=parkingOrderList.get(retIndex).getEndTime().getTime()){
                    retIndex--;
                }
                else{
                    hasExpired = true;
                }
                break;
            }
            retIndex++;
        }
        if(retIndex<0) {
            return null;
        }
        else{
            if(retIndex>=parkingOrderList.size())
                retIndex=parkingOrderList.size()-1;
            retMap.put("firstOrder",parkingOrderList.get(retIndex));
            retMap.put("hasExpired",hasExpired);
            if(hasExpired&&retIndex>0){
                retMap.put("secondOrder",parkingOrderList.get(retIndex-1));
            }
            else{
                retMap.put("secondOrder",null);
            }
            return retMap;
        }
    }


}
