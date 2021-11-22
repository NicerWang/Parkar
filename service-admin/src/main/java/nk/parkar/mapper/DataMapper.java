package nk.parkar.mapper;

import nk.parkar.pojo.DataItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface DataMapper {
    @Select("SELECT * FROM data WHERE time > #{start} AND time < #{end};")
    List<DataItem> getAllByTimeInterval(Date start, Date end);

    @Insert("INSERT INTO data VALUES(now(),#{type},#{userId},#{sa1},#{sa2},#{sa3},#{sa4});")
    int insert(DataItem dataItem);
}
