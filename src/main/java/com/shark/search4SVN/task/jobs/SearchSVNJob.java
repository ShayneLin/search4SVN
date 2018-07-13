package com.shark.search4SVN.task.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Author linchangshun
 * @Description 用于定时任务的详细工作任务，完成定时搜索指定的SVN文档，定时更新。
 * @Date Created in 16:39 2018/6/14
 */
public class SearchSVNJob implements Job{
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
