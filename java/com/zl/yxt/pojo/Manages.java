package com.zl.yxt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Manages {

    @TableId(type = IdType.AUTO)
    public Integer id;

    public String describe; //任务描述

    public String mname; //任务名称

    public String file; //附件地址

    public Integer tid; //教师编号

    public Integer type; //任务类型

    public Integer life; //是否截至

    public Date starttime; //开始时间

    public Date overtime; //结束时间

    @TableField(exist = false)
    public Integer cron; //时长（秒数）

    @TableField(exist = false)
    public String jobClass;

    @TableField(exist = false)
    public String classesvalue[];
}
