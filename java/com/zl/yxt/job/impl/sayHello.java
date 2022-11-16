package com.zl.yxt.job.impl;

import com.zl.yxt.job.BaseJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class sayHello implements BaseJob {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.err.println("=====================> Hello");
    }
}
