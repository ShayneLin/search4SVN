package com.shark.search4SVN.service.redis;

import com.shark.search4SVN.util.*;
import com.shark.search4SVN.service.wrapper.JedisAdapter;
import com.shark.search4SVN.service.wrapper.SolrAdapter;
import org.apache.log4j.Logger;

/**
 * Created by liuqinghua on 16-9-10.
 */
@Deprecated
public class ThreadSolrWorker implements Runnable {

    private static Logger logger = Logger.getLogger(ThreadSolrWorker.class);


    private JedisAdapter jedisAdapter;
    private SolrAdapter solrAdapter;


    public ThreadSolrWorker(){
        this.jedisAdapter = (JedisAdapter) Search4SVNContext.getBean(JedisAdapter.class);
        this.solrAdapter = (SolrAdapter)Search4SVNContext.getBean(SolrAdapter.class);
    }

    @Override
    public void run() {
        while(!ThreadScheduleService.isStop()) {
            try {
                ThreadUtils.sleep(5000);

                String solrDocStr = jedisAdapter.spop(Constants.SOLRDOC);
                logger.info("拔取的内容为 " + solrDocStr);

                //SVNDocument svnDoc = (SVNDocument) JSONObject.parse(solrDocStr);

                //solrAdapter.addSolrDocument(svnDoc);
            }catch(Exception e){
                logger.error(e.getMessage(), e);
                continue;
            }
        }
    }
}
