package com.zl.yxt.mapper.admin.sqlprovider;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

public class AdminUserSqlprovider {

    public String findUsers(Map<String, String> params){
        String sql = "SELECT a.*,s.scname FROM (SELECT u.*,c.cname FROM users u LEFT JOIN class c ON u.cid = c.id) a LEFT JOIN school s ON a.sid=s.id WHERE 1=1";
        StringBuilder sqlbuilder = new StringBuilder(sql);
        if (params.containsKey("cid") && params.get("cid").length()>0){
            sqlbuilder.append(" AND (a.id in (SELECT tid FROM `teacher-class` WHERE bid=#{cid}) OR a.cid=#{cid})");
        }
        if (params.containsKey("sid") && params.get("sid").length()>0){
            sqlbuilder.append(" AND a.sid=#{sid}");
        }
        if (params.containsKey("role") && params.get("role").length()>0){
            sqlbuilder.append(" AND a.role=#{role}");
        }
        if (params.containsKey("phone") && params.get("phone").length()>0){
            sqlbuilder.append(" AND a.phone=#{phone}");
        }
        if (params.containsKey("name") && params.get("name").length()>0){
            sqlbuilder.append(" AND a.name LIKE '%' #{name} '%'");
        }
        return sqlbuilder.toString();
    }

    public String getAllTeacherApply(String sid){
        String sql = "SELECT u.id,u.`name`,u.classid,u.sex,u.role,u.uid,u.cid,s.scname FROM usersapply u LEFT JOIN school s ON u.sid=s.id WHERE u.role='teacher' and u.isread='0'";
        StringBuilder sqlbuilder = new StringBuilder(sql);
        if (sid != null && sid != "") {
            sqlbuilder.append(" and u.sid=#{sid}");
        }
        return sqlbuilder.toString();
    }

    public String getApplyClassName(@Param("classid") String[] classid){
        if (classid.length>0){
            String sql = "select cname from class where id in(";
            StringBuilder sqlbuilder = new StringBuilder(sql);
            sqlbuilder.append(classid[0]);
            for (int i =1;i<classid.length;i++){
                sqlbuilder.append(","+classid[i]);
            }
            sqlbuilder.append(")");
            return sqlbuilder.toString();
        }else {
            return null;
        }
    }
}
