package com.zl.yxt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zl.yxt.pojo.Users;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<Users> {
    //获得个人信息
    Users getInformation(Integer id);

    //查询教师的班级
    @Select("select bid from `teacher-class` where tid=#{id}")
    String[] getClassIds(Integer id);
}
