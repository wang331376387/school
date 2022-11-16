package com.zl.yxt.job.impl;

import com.zl.yxt.job.BaseJob;
import com.zl.yxt.mapper.ManageMapper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;

public class OverManage implements BaseJob {

    @Autowired
    private ManageMapper manageMapper;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobkey=context.getJobDetail().getKey(); //获取job信息
        String jobName = jobkey.getName(); //job名称即任务名称

        manageMapper.OverManageByName(jobName); //结束任务
    }
}
