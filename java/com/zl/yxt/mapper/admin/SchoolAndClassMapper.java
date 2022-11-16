package com.zl.yxt.mapper.admin;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface SchoolAndClassMapper {

    @Select("select * from school")
    List<Map<String, Object>> getSchools();

    @Select("select * from class")
    List<Map<String, Object>> getClasses();

    @Select(("select * from class where sid=#{sid}"))
    List<Map<String, Object>> getClassesBySid(String sid);

    @Delete("delete from class where id=#{id}")
    void deleteClassById(String id);

    @Insert("insert into class (sid,cname) values (#{sid},#{cname})")
    void SaveClass(@Param("sid") String sid, @Param("cname") String cname);

    @Insert("insert into school (scname) values (#{scname})")
    void SaveSchool(String scname);

    @Select("select scname from school where id=#{sid}")
    String getSchoolById(String sid);
}
