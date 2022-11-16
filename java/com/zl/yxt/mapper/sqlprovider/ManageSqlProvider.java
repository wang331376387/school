package com.zl.yxt.mapper.sqlprovider;

import org.apache.ibatis.annotations.Param;

import java.text.MessageFormat;

public class ManageSqlProvider {

    //插入任务和班级中间数据
    public String saveManageClass(@Param("classesvalue") String[] classesvalue, @Param("mid") Integer mid) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO `manage-class` ");
        sql.append("(mid,cid) ");
        sql.append("VALUES ");
        MessageFormat mf = new MessageFormat("(#'{'mid},#'{'classesvalue[{0}]})");
        for (int i = 0; i < classesvalue.length; i++) {
            sql.append(mf.format(new Object[]{i}));
            if (i < classesvalue.length - 1) {
                sql.append(",");
            }
        }
        return sql.toString();
    }
}
