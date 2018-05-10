package com.shark.search4SVN.service.wrapper;

import com.shark.search4SVN.pojo.SVNDocument;
import com.shark.search4SVN.util.MessageUtil;
import com.shark.search4SVN.util.SolrConnProperties;
import com.shark.search4SVN.util.ThreadManager;
import com.shark.search4SVN.util.ThreadUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by liuqinghua on 16-9-13.
 */
@Service("solrAdapter")
public class SolrAdapter implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SolrAdapter.class);
    private SolrClient solrClient;

    private volatile boolean commiting2Solr = false;
    private Object commitingLock = new Object();
    private List<SVNDocument> submit2Solr = new ArrayList<SVNDocument>();

    @Autowired
    private SolrConnProperties solrConnProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        String url = solrConnProperties.getUrl();

        solrClient = new HttpSolrClient.Builder(url).build();

        //设置定时器，定时 提交文档到 solr
        Timer timer = new Timer();
        long delay = 0;
        long intevalPeriod = 2 * 1000;

        //定时提交任务
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    commiting2Solr = true;

                    boolean hasSubmit = false;
                    SolrInputDocument solrDoc = null;
                    List<SVNDocument> subDocs = new ArrayList<SVNDocument>();

                    for(SVNDocument svnDocument:submit2Solr){
                        subDocs.add(svnDocument);
                    }
                    submit2Solr.clear();
                    commiting2Solr = false;

                    for(SVNDocument document:subDocs){
                        hasSubmit = true;
                        try {
                            logger.info("提交文档 " + document.getDocName());
                            solrDoc = new SolrInputDocument();

                            solrDoc.addField("id", MessageUtil.md5(document.getDocName()));
                            solrDoc.addField("filename", document.getDocName());
                            solrDoc.addField("revision", document.getRevision());
                            solrDoc.addField("lastauthor", document.getLastModifyAuthor());
                            solrDoc.addField("lastModifyTime", document.getLastModifyTime());
                            solrDoc.addField("svnurl", document.getSvnUrl());
                            solrDoc.addField("filecontent", document.getContent());

                            solrClient.add(solrDoc);
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    }

                    if(hasSubmit) {
                        try {
                            solrClient.commit();
                        } catch (SolrServerException e) {
                            logger.error(e.getMessage(), e);
                        } catch (IOException e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }catch(Exception e){
                  logger.error(e.getMessage(), e);
                } finally {
                    commiting2Solr = false;
                    synchronized (commitingLock){
                        commitingLock.notify();
                    }
                }

            }
        }, delay, intevalPeriod);
    }

    /**
     * 该方法的调用是从 消息阻塞队列里获取 doc 提交，本身已经是 一个线程异步处理，所以无需同步处理
     * @param document
     */
    public void addSolrDocument(SVNDocument document){
         try{
             logger.info("准备提交文档 " + document.getDocName());
             while(commiting2Solr == true){
                 synchronized (commitingLock){
                     commitingLock.wait(500);
                 }
             }
             logger.info("已提交文档 " + document.getDocName());
             submit2Solr.add(document);
         }catch(Exception e){
             logger.error(e.getMessage(), e);
         }

    }

    /**
     * 方法废弃，根据 solr官方说法，尽量不要手动 optimize
     */
    @Deprecated
    public void optimize(){
        if(commiting2Solr != false){
            try {
                solrClient.optimize();
            } catch (SolrServerException e) {
                logger.error(e.getMessage(), e);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public List<SVNDocument> searchSolrDocment(String keyword) throws IOException, SolrServerException {

        List<SVNDocument> docs = new ArrayList<SVNDocument>();

        SolrQuery query = new SolrQuery("keywords:" + keyword);
        query.setFields("revision","svnurl","filename", "lastModifyTime", "lastModifyTime");
        //query.setSort("lastUpdateTime", ORDER.desc);
        query.setStart(0);
        query.setRows(50);
        QueryResponse response = solrClient.query(query);

        // 搜索得到的结果数
        //System.out.println("Find:" + response.getResults().getNumFound());
        // 输出结果
        for (SolrDocument doc : response.getResults()) {
            SVNDocument d = new SVNDocument();
            d.setRevision(doc.getFieldValue("revision").toString());
            d.setSvnUrl(doc.getFieldValue("svnurl").toString());
            d.setDocName(doc.getFieldValue("filename").toString());
            d.setLastModifyAuthor(doc.getFieldValue("lastModifyTime").toString());
            docs.add(d);
        }
        return docs;
    }
}
