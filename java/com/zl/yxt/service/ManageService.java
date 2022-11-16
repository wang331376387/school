package com.zl.yxt.service;

import com.zl.yxt.pojo.Manages;
import com.zl.yxt.pojo.vo.ResultVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

public interface ManageService {
    //创建任务
    @Transactional
    ResultVO createManage(Manages manages) throws Exception;

    //上传文件
    String uploadFile(MultipartFile file) throws Exception;

    //查询全部任务
    ResultVO showMyManageList(Map<String,String> map) throws UnsupportedEncodingException;

    //创建回复
    ResultVO CreateFeedBack(Map<String, String> param);

    //查看学生（1参与/0未参与）
    ResultVO showStudentList(Map<String, String> param);

    //查看学生回复
    ResultVO showFeedBackList(Map<String, String> param);

    //查看所有申请
    ResultVO getAllApply(String tid);

    //查询申请信息
    ResultVO getApplyById(Integer id);

    //同意学生审批
    @Transactional
    ResultVO acceptApply(Integer id) throws Exception;
}
