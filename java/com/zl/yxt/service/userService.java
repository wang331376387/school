package com.zl.yxt.service;

import com.zl.yxt.pojo.Users;
import com.zl.yxt.pojo.vo.ResultVO;

public interface userService {
    //验证登录
    ResultVO login(Users users);

    //根据Id获取个人信息
    Users getInformation(Integer id);

    //注册
    void register(Users user);
}
