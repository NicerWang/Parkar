package nk.parkar.mapper;


import nk.parkar.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {
    @Select("SELECT * FROM admin WHERE name = #{name}")
    Admin getByName(@Param("name") String name);
}
