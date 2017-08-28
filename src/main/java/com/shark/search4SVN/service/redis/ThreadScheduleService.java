package com.shark.search4SVN.service.redis;

import com.shark.search4SVN.service.wrapper.SVNAdapter;
import com.shark.search4SVN.util.*;
import com.shark.search4SVN.service.wrapper.JedisAdapter;
import org.springframework.stereotype.Service;

/**
 * Created by liuqinghua on 16-9-10.
 * 任务线程(scheduleThread)
 */
@Deprecated
@Service
public class ThreadScheduleService implements Runnable {

    private SVNAdapter svnService;
    private String url;

    private ThreadManager threadManager;


    public void init(String svnurl, String username, String pwd){
        this.url = svnurl;
        svnService = new SVNAdapter();
        svnService.init(username, pwd);
    }

    @Override
    public void run() {
        ThreadSVNDirWorker svnDirWorker = new ThreadSVNDirWorker(svnService);
        ThreadSVNFileWorker svnFileWorker = new ThreadSVNFileWorker(svnService);
        ThreadSolrWorker solrWorker = new ThreadSolrWorker();
        JedisAdapter jedisAdapter = (JedisAdapter) Search4SVNContext.getBean(JedisAdapter.class);
        jedisAdapter.sadd(Constants.SVNDIRKEY, url);

        threadManager.submitTask("1", svnDirWorker);
        threadManager.submitTask("2", svnFileWorker);
        threadManager.submitTask("3", solrWorker);
    }


    public static boolean isStop(){
        JedisAdapter jedisAdapter = (JedisAdapter) Search4SVNContext.getBean(JedisAdapter.class);
        long restDirNum = jedisAdapter.scard(Constants.SVNDIRKEY);
        long restFileNum = jedisAdapter.scard(Constants.SVNFILE);
        long restSolrDoc = jedisAdapter.scard(Constants.SOLRDOC);

        if(restDirNum > 0L || restFileNum > 0L || restSolrDoc > 0L){
            return false;
        }
        return true;
    }

    public void setThreadManager(ThreadManager threadManager) {
        this.threadManager = threadManager;
    }


}
