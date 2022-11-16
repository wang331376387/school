package com.zl.yxt.job.impl;

import com.zl.yxt.job.BaseJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.TriggerKey;

public class sendManage implements BaseJob {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        TriggerKey trkey = context.getTrigger().getKey(); //获取触发器信息
        JobKey jobkey=context.getJobDetail().getKey(); //获取job信息
        String classId = trkey.getGroup(); //分组编号，即班级编号
    }
}
