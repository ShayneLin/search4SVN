package com.shark.search4SVN.task.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Author linchangshun
 * @Description
 * @Date Created in 16:56 2018/5/25
 */
public class HelloJob implements Job {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Hello");
    }
}
