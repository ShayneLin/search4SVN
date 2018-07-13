package com.shark.search4SVN.task;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import java.util.Date;
import com.shark.search4SVN.task.jobs.HelloJob;
/**
 * @Author linchangshun
 * @Description
 * @Date Created in 16:54 2018/5/25
 * 定时任务
 */
public class UptateSolrDocumentTask {
    public static void main(String[] args) {


        try {
            //时间策略类，决定何时如何执行或者启动任务
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            System.out.println("do something here!");
            //TODO 做一些事情
            //为Job添加详细信息
            JobDetailImpl jobDetail = new JobDetailImpl();
            jobDetail.setName("hello");
            jobDetail.setGroup("001");
            jobDetail.setJobClass(HelloJob.class);
             /*//构建日期（用于指定什么时候执行）
            DateBuilder builder = DateBuilder.newDate();
            Date date = builder.atHourMinuteAndSecond(16,25,0).build();

           //触发器，用于指定时间，周期，重复次数的触发Job
            SimpleTriggerImpl trigger = new SimpleTriggerImpl("simpleTrigger","tigger-g1");
            //设置触发的时间
            trigger.setStartTime(date);
            //设置下一次触发的时间
            trigger.setNextFireTime(date);
            //触发后，2S的间隔执行一次
            trigger.setRepeatInterval(2000);
            //触发后，执行次数为5次
            trigger.setRepeatCount(5);
            System.out.println(trigger.getNextFireTime());*/
            CronTrigger trigger = (CronTrigger) TriggerBuilder
                    .newTrigger()//创建触发器
                    .withIdentity("testTrigger", "01")//设定标示
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))//添加表达式,每天夜晚12点开始触发
                    .build(); // 设置触发器
            scheduler.scheduleJob(jobDetail,trigger);
            //启动定时计划
            scheduler.start();
            //TODO 任务开始后，可以做些其他事情
            //scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }

}
