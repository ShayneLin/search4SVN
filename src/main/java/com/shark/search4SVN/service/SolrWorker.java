package com.shark.search4SVN.service;

import com.alibaba.fastjson.JSONObject;
import com.shark.search4SVN.pojo.SVNDocument;
import com.shark.search4SVN.util.*;
import org.apache.log4j.Logger;

/**
 * Created by liuqinghua on 16-9-10.
 */
public class SolrWorker implements Runnable {

    private static Logger logger = Logger.getLogger(SolrWorker.class);


    private JedisAdapter jedisAdapter;
    private SolrAdapter solrAdapter;


    public SolrWorker(){
        this.jedisAdapter = (JedisAdapter) Search4SVNContext.getBean(JedisAdapter.class);
        this.solrAdapter = (SolrAdapter)Search4SVNContext.getBean(SolrAdapter.class);
    }

    @Override
    public void run() {
        while(!ScheduleService.isStop()) {
            try {
                ThreadUtls.sleep(5000);

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
