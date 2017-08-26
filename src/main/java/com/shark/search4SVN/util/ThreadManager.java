package com.shark.search4SVN.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liuqinghua on 16-9-10.
 * 所有的线程交给其来处理
 */
@Deprecated
@Component
public class ThreadManager implements InitializingBean {

    private static Logger logger = Logger.getLogger(ThreadManager.class);

    private ExecutorService fixedThreadPool;

    @Override
    public void afterPropertiesSet() throws Exception {
        //根据 当前机器的配置来决定 线程的数量
        int threadNum = Math.max(2,Runtime.getRuntime().availableProcessors());
        logger.info("ThreadManager  num " + threadNum);
        fixedThreadPool = Executors.newFixedThreadPool(threadNum);
    }

    /**
     * 提交任务
     */
    public void submitTask(String uuid, Runnable task){
        fixedThreadPool.execute(task);
    }
}
