package com.zl.yxt.utils;

import com.zl.yxt.controller.WebSocket;
import com.zl.yxt.mapper.TalkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public class SendNotificationUtil {

    @Resource
    private TalkMapper talkMapper;

    @Autowired
    private WebSocket webSocket;

    /**
     * 发送通知
     * @param myid 被通知人编号
     * @param content
     */
    public void SendNoti(Integer myid,String content){
        createConversation(1,myid); // 创建跟管理员会话
        sendMessage(myid,1,content);
        //发送成功，通知前台
        String text=myid+" 消息已经发送！"+ UUID.randomUUID();
        webSocket.sendOneMessage(myid.toString(),text);
    }

    //创建跟管理员的会话
    //创建会话
    public void createConversation(Integer talkerid, Integer createrid) {
        //查询此会话是否存在
        int num = talkMapper.selectNum(talkerid,createrid);
        if (num > 0){
            return;
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
        }
    }
    //发送通知
    /**
     * 发送消息
     * @param cid 创建人
     * @param tid 谈话人
     * @param content 消息内容
     * @return
     */
    public void sendMessage(Integer cid,Integer tid,String content) {
        Integer ConversationId = talkMapper.selectId(cid,tid); //查询会话编号
        Map<String,Object> param = new HashMap<>();
        param.put("cid",ConversationId); //会话编号
        param.put("uid",1); //管理员发送
        Calendar calendar = Calendar.getInstance();
        // 将毫秒数设为0
        calendar.set(Calendar.MILLISECOND,0);
        Date nowDate = calendar.getTime(); //获取当前时间
        param.put("createtime",nowDate);
        param.put("content",content);
        talkMapper.sendMessage(param); //发送消息
        Map<String,Object> Conversation = new HashMap<>();
        Conversation.put("talkerid",tid);
        Conversation.put("createrid",cid);
        Conversation.put("recentlytime",nowDate);
        Conversation.put("recentlymes",content);
        talkMapper.updateConversation(Conversation); //给对方更新会话信息
    }
}
