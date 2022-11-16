package com.zl.yxt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zl.yxt.mapper.TalkMapper;
import com.zl.yxt.pojo.Moods;
import com.zl.yxt.pojo.vo.ResultVO;
import com.zl.yxt.service.TalkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class TalkServiceImpl implements TalkService {

    @Resource
    private TalkMapper talkMapper;

    //根据ID查询会话
    @Override
    public ResultVO showMyConversationsById(Integer createrid) {

        List<Map<String,Object>> conversations = talkMapper.showMyConversationsById(createrid);
        return ResultVO.success(conversations);
    }

    //查询自己的学生
    @Override
    public ResultVO showMyStudents(Integer id,String name) {
        List<Map<String,Object>> students = talkMapper.showMyStudents(id,name);
        return ResultVO.success(students);
    }

    //查询老师
    @Override
    public ResultVO showMyTeachers(Integer id, String name) {
        List<Map<String,Object>> teachers = talkMapper.showMyTeachers(id,name);
        return ResultVO.success(teachers);
    }

    //创建会话
    @Override
    public ResultVO createConversation(Integer talkerid, Integer createrid) {
        //查询此会话是否存在
        int num = talkMapper.selectNum(talkerid,createrid);
        if (num > 0){
            return ResultVO.customize(4040,null,"会话已经存在");
        }else {
            Map<String,Object> param = new HashMap<>();
            param.put("talkerid",talkerid);
            param.put("createrid",createrid);
            Calendar calendar = Calendar.getInstance();
            // 将毫秒数设为0
            calendar.set(Calendar.MILLISECOND,0);
            Date nowDate = calendar.getTime(); //获取当前时间
            param.put("recentlytime",nowDate);
            param.put("noread",0); //未读个数
            talkMapper.insertConversation(param);
            return ResultVO.success();
        }
    }

    //清空未读消息
    @Override
    public ResultVO clearNoRead(Map<String, Object> param) {
        talkMapper.clearNoRead(param);
        return ResultVO.success();
    }

    /**
     * 查询聊天记录
     * @param pageIndex
     * @param cid 发送人
     * @param tid 接收人
     * @return
     */
    @Override
    public ResultVO selectChatHistory(Integer pageIndex, Integer cid,Integer tid) {
        Integer ConversationId = talkMapper.selectId(cid,tid); //查询发送人会话编号
        Integer TalkerConversationId = talkMapper.selectId(tid,cid); //查询接收人会话编号
        //分页查询
        PageHelper.startPage(pageIndex, 9);
        //倒叙排列的数据
        List<Map<String,Object>> chats = talkMapper.selectChatHistory(ConversationId,TalkerConversationId,tid);
        PageInfo<Map<String,Object>> data = new PageInfo<>(chats);
        List<Map<String,Object>> newChats = new ArrayList<>();
        int i=data.getList().size()-1;
        while (i>=0){ //再正序排列
            newChats.add(data.getList().get(i));
            i = i-1;
        }
        return ResultVO.success(newChats);
    }

    /**
     * 发送消息
     * @param cid 创建人
     * @param tid 谈话人
     * @param content 消息内容
     * @return
     */
    @Override
    public ResultVO sendMessage(Integer cid,Integer tid,String content) {
        Integer ConversationId = talkMapper.selectId(cid,tid); //查询会话编号
        Map<String,Object> param = new HashMap<>();
        param.put("cid",ConversationId); //会话编号
        param.put("uid",cid);
        Calendar calendar = Calendar.getInstance();
        // 将毫秒数设为0
        calendar.set(Calendar.MILLISECOND,0);
        Date nowDate = calendar.getTime(); //获取当前时间
        param.put("createtime",nowDate);
        param.put("content",content);
        //首先检查被发送方是否存在会话，不存在为其创建,注意tid与cid位置相反代表对方
        ResultVO conversation = this.createConversation(cid,tid);
        talkMapper.sendMessage(param); //发送消息
        Map<String,Object> Conversation = new HashMap<>();
        Conversation.put("talkerid",cid);
        Conversation.put("createrid",tid); //调换创建人和谈话人
        Conversation.put("recentlytime",nowDate);
        Conversation.put("recentlymes",content);
        talkMapper.updateConversation(Conversation); //给对方更新会话信息
        return ResultVO.success();
    }

    //所有未读
    @Override
    public ResultVO selectnoread(Integer uid) {
        Integer noread = talkMapper.selectnoread(uid);
        return ResultVO.success(noread);
    }
}
