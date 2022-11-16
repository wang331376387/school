package com.zl.yxt.mapper.admin;

import com.zl.yxt.mapper.admin.sqlprovider.AdminUserSqlprovider;
import com.zl.yxt.pojo.Users;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface AdminUserMapper {

    //查询管理员
    @Select("select * from users where phone=#{username} and password=#{password} and role='root'")
    Users findAdmin(@Param("username") String username,@Param("password") String password);

    //按条件查询用户
    @SelectProvider(type= AdminUserSqlprovider.class,method="findUsers")
    List<Map<String, Object>> findUsers(Map<String, String> params);

    //根据教师编号查询归属班级
    @Select("SELECT cname FROM class WHERE id in (\n" +
            "SELECT bid FROM `teacher-class` WHERE tid=#{id})")
    List<String> findCnameByTid(String id);

    //查询学校对应班级信息
    @Select("SELECT cname,id FROM class WHERE sid=#{id}")
    List<Map<String, Object>> selectMyClasses(Integer id);

    //清空认证
    @Update("UPDATE users SET role=null,sid=null,cid=null WHERE id=#{id}")
    void ClearCertification(Integer id);

    //查询电话号码存在数量，避免重复
    @Select("SELECT count(id) FROM users WHERE phone=#{phone}")
    int checkPhoneExists(String phone);

    //创建管理员
    @Insert("INSERT INTO users (`name`,username,sex,phone,role) VALUES (#{name},#{username},#{sex},#{phone},#{role})")
    void createRoot(Users users);
}
