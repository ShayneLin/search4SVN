package com.shark.search4SVN.util;

import com.shark.search4SVN.service.ScheduleService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liuqinghua on 16-9-10.
 * 所有的线程交给其来处理
 */
@Component
public class ThreadManager implements InitializingBean {

    private static Logger logger = Logger.getLogger(ThreadManager.class);

    private ExecutorService fixedThreadPool;

    @Override
    public void afterPropertiesSet() throws Exception {
        //根据 当前机器的配置来决定 线程的数量
        int threadNum = Runtime.getRuntime().availableProcessors();

        fixedThreadPool = Executors.newFixedThreadPool(threadNum);
    }

    /**
     * 提交任务
     */
    public void submitTask(String uuid, Runnable task){
        fixedThreadPool.submit(task);
    }
}
