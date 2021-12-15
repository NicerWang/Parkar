package nk.parkar.service.impl;

import nk.parkar.mapper.MRMapper;
import nk.parkar.pojo.*;
import nk.parkar.service.MapReduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MapReduceServiceImpl implements MapReduceService {

    private MRMapper mrMapper;
    private File file0;
    private File file10;
    private File file11;
    private File file12;
    private File file20;
    private File file21;
    private File file22;
    private long startNum;
    @Autowired
    public void setMrMapper(MRMapper mrMapper) {
        this.mrMapper = mrMapper;
    }
    @Autowired
    public void setFile(){
        this.file0 = new File("map-result0");
        this.file10 = new File("map-result10");
        this.file11 = new File("map-result11");
        this.file12 = new File("map-result12");
        this.file20 = new File("map-result20");
        this.file21 = new File("map-result21");
        this.file22 = new File("map-result22");
    }

    private void initStartNum(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY , 0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        startNum = calendar.getTime().getTime()-86400000;
    }

    private int mapProcess(Date start,Date end){
        int dataCount = mrMapper.getDataCountByTimeInterval(start,end);
        try{
            file0.deleteOnExit();
            file10.deleteOnExit();
            file11.deleteOnExit();
            file12.deleteOnExit();
            file20.deleteOnExit();
            file21.deleteOnExit();
            file22.deleteOnExit();
            file0.createNewFile();
            file10.createNewFile();
            file11.createNewFile();
            file12.createNewFile();
            file20.createNewFile();
            file21.createNewFile();
            file22.createNewFile();

            FileWriter fileWriter0 = new FileWriter(file0.getName(),true);
            FileWriter fileWriter10 = new FileWriter(file10.getName(),true);
            FileWriter fileWriter11 = new FileWriter(file11.getName(),true);
            FileWriter fileWriter12 = new FileWriter(file12.getName(),true);
            FileWriter fileWriter20 = new FileWriter(file20.getName(),true);
            FileWriter fileWriter21 = new FileWriter(file21.getName(),true);
            FileWriter fileWriter22 = new FileWriter(file22.getName(),true);

            for(int i=0;i<dataCount;i+=1000){
                int limit = 1000;
                List<DataItem> dataItemList = mrMapper.getLimitDataByTimeInterval(start,end,i,limit);
                for (DataItem dataItem : dataItemList) {
                    String outStr = dataItem.toString() + "\n";
                    switch (dataItem.getType()) {
                        case 0:
                            fileWriter0.write(outStr);
                            break;
                        case 10:
                            fileWriter10.write(outStr);
                            break;
                        case 11:
                            fileWriter11.write(outStr);
                            break;
                        case 12:
                            fileWriter12.write(outStr);
                            break;
                        case 20:
                            fileWriter20.write(outStr);
                            break;
                        case 21:
                            fileWriter21.write(outStr);
                            break;
                        case 22:
                            fileWriter22.write(outStr);
                            break;
                    }
                }
            }
            fileWriter0.close();
            fileWriter10.close();
            fileWriter11.close();
            fileWriter12.close();
            fileWriter20.close();
            fileWriter21.close();
            fileWriter22.close();

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return 0;
    }

    private int reduceProcess(Date end) {
        int reservation = 0;
        int cancellation = 0;
        double activity = 0;

        if(file0.exists()&&file10.exists()&&file11.exists()&&file12.exists()&&
                file20.exists()&&file21.exists()&&file22.exists()){
            try{
                //处理0 登录
                BufferedReader reader = new BufferedReader(new FileReader(file0));
                while((reader.readLine())!=null){
                    activity +=1;
                }
                reader.close();

                //处理10 查询订单
                reader = new BufferedReader(new FileReader(file10));
                while((reader.readLine())!=null){
                    activity +=0.2;
                }
                reader.close();

                //处理11 取消订单
                reader = new BufferedReader(new FileReader(file11));
                while((reader.readLine())!=null){
                    cancellation += 1;
                    activity+=0.5;
                }
                reader.close();

                //处理12 预约订单
                reader = new BufferedReader(new FileReader(file12));
                while((reader.readLine())!=null){
                    reservation += 1;
                    activity+=1;
                }
                reader.close();

                //处理20 查询车牌
                reader = new BufferedReader(new FileReader(file20));
                while((reader.readLine())!=null){
                    activity+=0.2;
                }
                reader.close();

                //处理21 删除车牌
                reader = new BufferedReader(new FileReader(file21));
                while((reader.readLine())!=null){
                    activity+=0.5;
                }
                reader.close();

                //处理22 添加车牌
                reader = new BufferedReader(new FileReader(file22));
                while((reader.readLine())!=null){
                    activity+=0.5;
                }
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            //写入reservation
            MRReservation mrReservation = new MRReservation();
            mrReservation.setTime(end);
            mrReservation.setValue(reservation);
            mrMapper.insertReservation(mrReservation);

            //写入cancellation
            MRCancellation mrCancellation = new MRCancellation();
            mrCancellation.setTime(end);
            mrCancellation.setValue(cancellation);
            mrMapper.insertCancellation(mrCancellation);

            //写入activity
            MRActivity mrActivity = new MRActivity();
            mrActivity.setTime(end);
            BigDecimal tempBD = new BigDecimal(activity);
            activity = tempBD.setScale(2, RoundingMode.HALF_UP).doubleValue();
            mrActivity.setValue(activity);
            mrMapper.insertActivity(mrActivity);

            //写入avail
            DataItem dataItem = mrMapper.getLatestAvail(end);
            MRAvail mrAvail = new MRAvail();
            mrAvail.setTime(end);
            double avail;
            if(dataItem!=null)
                 avail = Double.parseDouble(dataItem.getSa1());
            else
                avail = 0;
            tempBD = new BigDecimal(avail);
            avail = tempBD.setScale(2, RoundingMode.HALF_UP).doubleValue();
            mrAvail.setValue(avail);
            mrMapper.insertAvail(mrAvail);

            file0.delete();
            file10.delete();
            file11.delete();
            file12.delete();
            file20.delete();
            file21.delete();
            file22.delete();
        }
        else{
            return -1;
        }
        return 0;
    }

    @Override
    @Transactional
    public int mapReduce() {
        initStartNum();
        MRActivity lastActivity = mrMapper.getLastActivity();
        if(lastActivity==null||lastActivity.getTime().getTime()<=startNum){
            long currentStartNum;
            if(lastActivity==null){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mrMapper.getEarliestData().getTime());
                calendar.set(Calendar.HOUR_OF_DAY , 0);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
                currentStartNum = calendar.getTime().getTime();
            }
            else{
                currentStartNum = lastActivity.getTime().getTime();
            }
            while(currentStartNum<=startNum){
                Date start = new Date(currentStartNum);
                Date end = new Date(currentStartNum+86400000);
                mapProcess(start, end);
                reduceProcess(end);
                currentStartNum+=86400000;
            }
        }
        else{
            System.err.println("No new data needs MapReduce!");
        }
        return 0;
    }

    @Override
    public ArrayList<MRActivity> getActivityList() {
        return (ArrayList<MRActivity>)mrMapper.getAllActivity();
    }

    @Override
    public ArrayList<MRAvail> getAvailList() {
        return (ArrayList<MRAvail>)mrMapper.getAllAvail();
    }

    @Override
    public ArrayList<MRCancellation> getCancellationList() {
        return (ArrayList<MRCancellation>)mrMapper.getAllCancellation();
    }

    @Override
    public ArrayList<MRReservation> getReservationList() {
        return (ArrayList<MRReservation>)mrMapper.getAllReservation();
    }
}
