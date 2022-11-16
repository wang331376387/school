package com.zl.yxt.controller;

import com.zl.yxt.controller.admin.users.AdminUserService;
import com.zl.yxt.pojo.vo.ResultVO;
import com.zl.yxt.service.TalkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/talk")
public class talkController {

    @Autowired
    private TalkService talkService;

    @Autowired
    private WebSocket webSocket;

    @Autowired
    private AdminUserService adminUserService;

    /**
     * 查询会话
     * @param createrid 创建人编号
     * @return
     */
    @RequestMapping(value = "/showMyConversation",method = RequestMethod.POST)
    public ResultVO selectConversation(Integer createrid){
        ResultVO data = talkService.showMyConversationsById(createrid);
        return data;
    }

    /**
     * 查询可以会话的人
     * @param id
     * @param role 角色
     * @return
     */
    @RequestMapping(value = "/selectTalker",method = RequestMethod.POST)
    public ResultVO selectTalker(Integer id,String role,String name){
        if (role.equals("teacher")){
            //查询自己的学生
            ResultVO data = talkService.showMyStudents(id,name);
            return data;
        }else {
            //查询自己的老师
            ResultVO data = talkService.showMyTeachers(id,name);
            return data;
        }
    }

    /**
     * 如果会话不存在创建会话
     * @param talkerid
     * @param createrid
     * @return
     */
    @RequestMapping(value = "/createConversation",method = RequestMethod.POST)
    public ResultVO createConversation(Integer talkerid,Integer createrid){
        ResultVO data = talkService.createConversation(talkerid,createrid);
        return data;
    }

    /**
     * 清空未读条数
     * @param cid
     * @param tid
     * @return
     */
    @RequestMapping(value = "/clearNoRead/{createrid}/{talkerid}",method = RequestMethod.GET)
    public ResultVO clearNoRead(@PathVariable("createrid") String cid,
                                @PathVariable("talkerid") String tid){
        Map<String,Object> param = new HashMap<>();
        param.put("cid",cid);
        param.put("tid",tid);
        ResultVO data = talkService.clearNoRead(param);
        return data;
    }

    /**
     * 查询聊天记录
     * @param pageIndex
     * @param cid
     * @return
     */
    @RequestMapping(value = "/selectChatHistory",method = RequestMethod.POST)
    public ResultVO selectChatHistory(@RequestParam(defaultValue = "1") Integer pageIndex,Integer cid,Integer tid){
        ResultVO data = talkService.selectChatHistory(pageIndex,cid,tid);
        return data;
    }

    /**
     * 发送消息
     * @param cid 用户id/创建人
     * @param tid 会话人id
     * @param content
     * @return
     */
    @RequestMapping(value = "/sendMessage",method = RequestMethod.POST)
    public ResultVO sendMessage(Integer cid,Integer tid,String content){
        ResultVO data = talkService.sendMessage(cid,tid,content);
        //发送成功，通知前台
        String text=tid+" 消息已经发送！"+ UUID.randomUUID();
        webSocket.sendOneMessage(tid.toString(),text);
        return data;
    }

    /**
     * 查询总未读个数
     * @param uid
     * @return
     */
    @RequestMapping(value = "/selectnoread",method = RequestMethod.POST)
    public ResultVO selectnoread(Integer uid){
        ResultVO data = talkService.selectnoread(uid);
        return data;
    }
    /**
     * 清空认证
     * @param id
     * @return
     */
    @PostMapping("/ClearCertification")
    @ResponseBody
    public ResultVO ClearCertification(Integer id){
        adminUserService.ClearCertification(id);
        return ResultVO.success();
    }
}
