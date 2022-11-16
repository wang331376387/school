package com.zl.yxt.mapper.admin;

import com.zl.yxt.mapper.admin.sqlprovider.AdminUserSqlprovider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

public interface TeacherApplyMapper {

    //按条件查询用户
    @SelectProvider(type= AdminUserSqlprovider.class,method="getAllTeacherApply")
    List<Map<String, Object>> getAllTeacherApply(String sid);

    @SelectProvider(type= AdminUserSqlprovider.class,method="getApplyClassName")
    List<String> getApplyClassName(@Param("classid") String[] classid);
}
