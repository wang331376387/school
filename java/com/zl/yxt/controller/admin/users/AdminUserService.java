package com.zl.yxt.controller.admin.users;

import com.zl.yxt.pojo.Users;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface AdminUserService {
    //查询用户
    List<Map<String, Object>> findUsers(Map<String, String> params);

    //查询学校对应班级信息
    List<Map<String, Object>> selectMyClasses(Integer id);

    //清空认证
    void ClearCertification(Integer id);

    //电话号码存在个数
    int checkPhoneExists(String phone);

    //创建管理员
    void createRoot(Users users);

    //查询管理员
    Users findAdmin(String username, String password);
}
