package com.zl.yxt.mapper.sqlprovider;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

public class TalkSqlProvider {

    //生成查询学生的sql
    public String showMyStudents(@Param("id") Integer id, @Param("name") String name){
        String sql = "SELECT id,`name`,photo FROM users WHERE cid in (SELECT distinct bid FROM `teacher-class` WHERE tid=#{id})";
        StringBuilder sqlbuilder = new StringBuilder(sql);
        if (name != null && name != ""){
            sqlbuilder.append(" AND `name` LIKE '%' #{name} '%'");
        }
        return sqlbuilder.toString();
    }

    //生成查询老师的sql
    public String showMyTeachers(@Param("id") Integer id, @Param("name") String name){
        String sql = "SELECT id,`name`,photo,cid FROM users WHERE id in (SELECT tid FROM `teacher-class` WHERE bid=(SELECT cid from users WHERE id=#{id}))";
        StringBuilder sqlbuilder = new StringBuilder(sql);
        if (name != null && name != ""){
            sqlbuilder.append(" AND `name` LIKE '%' #{name} '%'");
        }
        return sqlbuilder.toString();
    }

    //生成清空未读的sql
    public String clearNoRead(Map<String, Object> param){
        String sql = "UPDATE conversation SET noread=0 WHERE ";
        StringBuilder sqlbuilder = new StringBuilder(sql);
        //前台传值“0”证明是系统消息
        if (param.get("cid") != null && !param.get("cid").equals("0")){
            sqlbuilder.append("createrid=#{cid}");
        }else {
            sqlbuilder.append("createrid IS NULL");
        }
        sqlbuilder.append(" AND talkerid=#{tid}");
        return sqlbuilder.toString();
    }

    //生成聊天记录的sql
    public String selectChatHistory(@Param("ConversationId") Integer ConversationId,@Param("TalkerConversationId") Integer TalkerConversationId,@Param("tid") Integer tid){
        String sql = "SELECT c.*,u.`name`,u.photo FROM chathistory c ,users u " +
                "WHERE c.cid in(#{ConversationId},#{TalkerConversationId}) AND c.uid=u.id ORDER BY createtime DESC";
        return sql;
    }

    //生成查询会话编号的sql
    public String selectId(@Param("cid") Integer cid,@Param("tid") Integer tid){
        String sql = "SELECT id FROM conversation WHERE 1=1 ";
        StringBuilder sqlbuilder = new StringBuilder(sql);
        if (cid == null || cid == 0){ //查询系统会话编号
            //查询系统会话
            sqlbuilder.append(" AND createrid IS NULL AND talkerid=0");
        }else {
            sqlbuilder.append(" AND createrid=#{cid} AND talkerid=#{tid}");
        }
        return sqlbuilder.toString();
    }
}
