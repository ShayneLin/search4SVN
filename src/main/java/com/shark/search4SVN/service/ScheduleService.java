package com.shark.search4SVN.service;

import com.shark.search4SVN.util.Constants;
import com.shark.search4SVN.util.JedisAdapter;
import com.shark.search4SVN.util.Search4SVNContext;
import com.shark.search4SVN.util.ThreadManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuqinghua on 16-9-10.
 * 任务线程(scheduleThread)
 */
@Service
public class ScheduleService implements Runnable {

    private SVNService svnService;
    private String url;

    @Autowired
    private ThreadManager threadManager;


    public void init(String svnurl, String username, String pwd){
        this.url = url;
        svnService = new SVNService();
        svnService.init(username, pwd);
    }

    @Override
    public void run() {
        SVNDirWorker svnDirWorker = new SVNDirWorker(svnService);
        SVNFileWorker svnFileWorker = new SVNFileWorker(svnService);
        SolrWorker solrWorker = new SolrWorker();
        JedisAdapter jedisAdapter = (JedisAdapter) Search4SVNContext.getBean(JedisAdapter.class);
        jedisAdapter.sadd(Constants.SOLRDOC, url);

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
