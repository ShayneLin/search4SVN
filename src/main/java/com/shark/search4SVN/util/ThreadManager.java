package com.shark.search4SVN.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * Created by liuqinghua on 16-9-10.
 * 所有的线程交给其来处理
 */

public class ThreadManager {

    private ThreadManager(){}

    //内部类
    private static class ThreadManagerHolder{
        private static ThreadManager instance = new ThreadManager();
    }

    public static ThreadManager getInstance() {
        return ThreadManagerHolder.instance;
    }

    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            1,
            //最大线程池大小为1（有限数值）：
            Runtime.getRuntime().availableProcessors() * 2,
            60, TimeUnit.SECONDS,
            //工作队列为SynchronousQueue：
            new SynchronousQueue<Runnable>(),
            //线程池饱和处理策略为CallerRunsPolicy：
            new ReEnqueueRejectedExecutionHandler());


    /**
     * 提交任务
     */
    public void submitTask(Runnable task){
        threadPool.submit(task);
    }
}

/**
 * 该线程池饱和处理策略支持将提交失败的任务重新放入线程池工作队列。
 *
 * @author Viscent Huang
 *
 */
class ReEnqueueRejectedExecutionHandler implements
        RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (executor.isShutdown()) {
            return;
        }

        try {
            executor.getQueue().put(r);
        } catch (InterruptedException e) {
            ;
        }

    }

}
