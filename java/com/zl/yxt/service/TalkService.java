package com.zl.yxt.service;

import com.zl.yxt.pojo.vo.ResultVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface TalkService {
    //根据ID查询会话
    ResultVO showMyConversationsById(Integer createrid);

    //查询自己的学生
    ResultVO showMyStudents(Integer id,String name);

    //查询自己的老师
    ResultVO showMyTeachers(Integer id,String name);

    //创建会话
    ResultVO createConversation(Integer talkerid, Integer createrid);

    //清空未读
    ResultVO clearNoRead(Map<String, Object> param);

    //查询聊天记录
    ResultVO selectChatHistory(Integer pageIndex, Integer cid,Integer tid);

    //发送消息
    @Transactional
    ResultVO sendMessage(Integer cid,Integer tid, String content);

    //所有未读
    ResultVO selectnoread(Integer uid);
}
