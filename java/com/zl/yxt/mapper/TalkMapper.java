package com.zl.yxt.mapper;

import com.zl.yxt.mapper.sqlprovider.TalkSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface TalkMapper {

    //根据（登录人）创建者ID查询会话
    @Select("SELECT c.*,u.`name`,u.photo FROM conversation c LEFT JOIN users u ON c.talkerid=u.id WHERE c.createrid IS NULL OR c.createrid=#{createrid} ORDER BY c.recentlytime DESC")
    List<Map<String,Object>> showMyConversationsById(Integer createrid);

    //查询我的学生
    @SelectProvider(type= TalkSqlProvider.class,method="showMyStudents")
    List<Map<String, Object>> showMyStudents(@Param("id") Integer id,@Param("name") String name);

    //查询我的老师
    @SelectProvider(type= TalkSqlProvider.class,method="showMyTeachers")
    List<Map<String, Object>> showMyTeachers(@Param("id") Integer id,@Param("name") String name);

    //查询此会话是否存在
    @Select("SELECT COUNT(1) FROM conversation WHERE createrid=#{createrid} AND talkerid=#{talkerid}")
    int selectNum(@Param("talkerid") Integer talkerid,@Param("createrid") Integer createrid);

    //创建会话
    @Insert("INSERT INTO conversation (createrid,talkerid,noread,recentlytime) VALUES (#{createrid},#{talkerid},#{noread},#{recentlytime})")
    void insertConversation(Map<String, Object> param);

    //清空未读消息
    @SelectProvider(type= TalkSqlProvider.class,method="clearNoRead")
    void clearNoRead(Map<String, Object> param);

    //查询聊天记录
    @SelectProvider(type= TalkSqlProvider.class,method="selectChatHistory")
    List<Map<String, Object>> selectChatHistory(@Param("ConversationId") Integer ConversationId,@Param("TalkerConversationId") Integer TalkerConversationId,@Param("tid") Integer tid);

    //发送消息
    @Insert("INSERT INTO `chathistory` (`cid`, `content`, `createtime`, `uid`) VALUES ( #{cid}, #{content}, #{createtime}, #{uid})")
    void sendMessage(Map<String, Object> param);

    //查询会话ID
    @SelectProvider(type= TalkSqlProvider.class,method="selectId")
    Integer selectId(@Param("cid") Integer cid,@Param("tid") Integer tid);

    //更新会话信息
    @Update("UPDATE conversation SET `noread`=noread+1, `recentlytime`=#{recentlytime}, `recentlymes`=#{recentlymes} WHERE createrid=#{createrid} AND talkerid=#{talkerid}")
    void updateConversation(Map<String, Object> param);

    //查询所有未读
    @Select("select sum(noread) from conversation where createrid=#{uid}")
    Integer selectnoread(Integer uid);
}
