package com.zl.yxt.mapper;

import com.zl.yxt.pojo.School;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface InformationMapper {
    //获取班级和年级信息
    List<School> getSchoolAndClass();

    //更新个人信息（认证）
    void updateInfo(Map<String, Object> map);

    //查询我的班级
    @Select("SELECT cname,id FROM class WHERE id in (SELECT bid FROM `teacher-class` WHERE tid=#{id})")
    List<Map<String,Object>> selectMyClasses(Integer id);

    //添加教师班级
    void insertClasses(@Param("id") String id,@Param("cid") String[] cid);

    //删除原班级
    @Delete("DELETE FROM `teacher-class` WHERE tid=#{id}")
    void deleteClasses(String id);

    //查询头像名称个数
    @Select("SELECT COUNT(id) total FROM users WHERE photo LIKE CONCAT(#{preOriginalFilename},'%',#{fixOriginalFilename})")
    Integer findFile(String preOriginalFilename, String fixOriginalFilename);

    //保存认证申请
    @Insert("INSERT INTO usersapply (role,`name`,uid,cid,classid,sex,sid,isread) VALUES (#{role},#{name},#{uid},#{cid},#{classid},#{sex},#{sid},#{isread})")
    void insertApply(Map<String, String> information);

    //查询申请人数量
    @Select("select count(1) from usersapply where uid=#{uid}")
    int findUserApply(String uid);

    //更新申请
    @Update("update usersapply set role=#{role},`name`=#{name},uid=#{uid},cid=#{cid},classid=#{classid},sex=#{sex},sid=#{sid},isread=#{isread} where uid=#{uid}")
    void updateApply(Map<String, String> information);

    //更新头像
    @Update("update users set photo=#{picture} where id=#{id}")
    void updateAvatar(@Param("id") String id, @Param("picture") String picture);
}
