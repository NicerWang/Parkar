package nk.parkar.mapper;

import nk.parkar.pojo.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface MRMapper {
    @Select("SELECT COUNT(*) FROM data WHERE time > #{start} AND time < #{end};")
    int getDataCountByTimeInterval(Date start, Date end);
    @Select("SELECT * FROM data WHERE time > #{start} AND time <= #{end} limit #{index}, #{num};")
    List<DataItem> getLimitDataByTimeInterval(Date start, Date end, int index, int num);

    @Select("SELECT * FROM data WHERE type=4 and time < #{end} order by time desc limit 1;")
    DataItem getLatestAvail(Date end);
    @Select("SELECT * FROM data order by time asc limit 1;")
    DataItem getEarliestData();
    @Select("SELECT * FROM mr_activity order by `time` desc limit 1;")
    MRActivity getLastActivity();

    @Select("SELECT * FROM mr_activity;")
    List<MRActivity> getAllActivity();
    @Select("SELECT * FROM mr_cancellation;")
    List<MRCancellation> getAllCancellation();
    @Select("SELECT * FROM mr_reservation;")
    List<MRReservation> getAllReservation();
    @Select("SELECT * FROM mr_avail;")
    List<MRAvail> getAllAvail();


    @Insert("INSERT INTO mr_avail(`time`,`value`) value(#{time},#{value});")
    int insertAvail(MRAvail mrAvail);
    @Insert("INSERT INTO mr_reservation(`time`,`value`) value(#{time},#{value});")
    int insertReservation(MRReservation mrReservation);
    @Insert("INSERT INTO mr_cancellation(`time`,`value`) value(#{time},#{value});")
    int insertCancellation(MRCancellation mrCancellation);
    @Insert("INSERT INTO mr_activity(`time`,`value`) value(#{time},#{value});")
    int insertActivity(MRActivity mrActivity);

}
