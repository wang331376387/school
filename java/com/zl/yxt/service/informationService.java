package com.zl.yxt.service;

import com.zl.yxt.pojo.vo.ResultVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface informationService {

    //获取年级和班级信息
    ResultVO getSchoolAndClass();

    //更新信息
    @Transactional
    ResultVO updateInformation(Map<String, Object> information,String cid[]) throws Exception;

    //查询我的班级
    ResultVO selectMyClasses(Integer id);

    //保存认证申请
    ResultVO certifyApply(Map<String, String> information);

    //更改头像
    @Transactional
    ResultVO changeAvatar(String id) throws Exception;
}
