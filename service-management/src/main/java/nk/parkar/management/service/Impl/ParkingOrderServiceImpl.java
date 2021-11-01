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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParkingOrderServiceImpl implements ParkingOrderService {

    @Autowired
    ParkingOrderMapper parkingOrderMapper;
    @Autowired
    ParkingTimeMapper parkingTimeMapper;

    @Override
    @Transactional
    public Map<String,Object> insertOrderFromUser(Integer userId, Integer spaceId, String mode, Long startTime, Long endTime) {

        TransactionException transactionException = new TransactionException("insertOrderFromUser");
        List<ParkingTime> parkingTimeList = parkingTimeMapper.selectBySpaceId(spaceId);
        if(!parkingTimeList.isEmpty()){
            for(ParkingTime parkingTime:parkingTimeList){
                Long tempStartTime = parkingTime.getStartTime().getTime()/1000;
                Long tempEndTime = parkingTime.getEndTime().getTime()/1000;

                if((tempStartTime.compareTo(endTime)<0&&tempStartTime.compareTo(startTime)>0)||
                        (tempEndTime.compareTo(endTime)<0&&tempEndTime.compareTo(startTime)>0)||
                        (tempEndTime.compareTo(endTime)==0&&tempStartTime.compareTo(startTime)==0)){
                    transactionException.addDescription("the time period in the space has been occupied");
                    throw transactionException;
                }
            }
        }
        BigDecimal unitPrice = new BigDecimal("0");
        Integer modeCode = 0;
        BigDecimal totalPrice;
        Long tempTimeUnits = 0l;
        Long dis = endTime-startTime;
        if(mode.equals("day")){
            unitPrice =new BigDecimal("4.0");
            tempTimeUnits = dis-3600*(dis/3600)>60?dis/3600+1:dis/3600;
        }
        else if(mode.equals("month")){
            unitPrice =new BigDecimal("1.5");
            modeCode=1;
            tempTimeUnits=dis/3600;
        }
        else if(mode.equals("year")){
            unitPrice =new BigDecimal("1.2");
            modeCode = 2;
            tempTimeUnits=dis/3600;
        }
        BigDecimal timeUnits = new BigDecimal(tempTimeUnits);
        totalPrice = timeUnits.multiply(unitPrice);

        ParkingOrder parkingOrder = new ParkingOrder();
        parkingOrder.setMode(modeCode);
        parkingOrder.setPaid((byte) 0);
        parkingOrder.setEndTime(new Date(endTime*1000));
        parkingOrder.setStartTime(new Date(startTime*1000));
        parkingOrder.setUserId(userId.toString());
        parkingOrder.setPrice(totalPrice);
        parkingOrder.setSpaceId(spaceId);
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
}
