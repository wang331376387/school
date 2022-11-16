package com.zl.yxt.utils;

import com.zl.yxt.job.BaseJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class QuartzUtils {

    @Autowired
    @Qualifier("Scheduler")
    private Scheduler scheduler;

    //创建任务（任务和触发器共用名称和分组）
    public void addJob(String jobName, String jobGroup,String jobClass, Date overTime)throws Exception{
        // 启动调度器
        scheduler.start();
        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(getClass(jobClass).getClass())
                .withIdentity(jobName, jobGroup).build();

        //cron秒后执行一次且只执行一次 .withIntervalInSeconds(cron)
        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
                .startAt(overTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withRepeatCount(0)
                ).build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    public BaseJob getClass(String classname) throws Exception
    {
        Class<?> class1 = Class.forName(classname);
        return (BaseJob)class1.newInstance();
    }

}
