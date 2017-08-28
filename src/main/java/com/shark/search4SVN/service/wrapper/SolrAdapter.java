package com.shark.search4SVN.service.wrapper;

import com.shark.search4SVN.pojo.SVNDocument;
import com.shark.search4SVN.util.MessageUtil;
import com.shark.search4SVN.util.SolrConnProperties;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuqinghua on 16-9-13.
 */
@Service("solrAdapter")
public class SolrAdapter implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SolrAdapter.class);
    private SolrClient solrClient;

    @Autowired
    private SolrConnProperties solrConnProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        String url = solrConnProperties.getUrl();

        solrClient = new HttpSolrClient.Builder(url).build();
    }

    public void addSolrDocument(SVNDocument document){
         SolrInputDocument solrDoc = null;
         try{
             logger.info("提交文档 " + document.toString());
             solrDoc = new SolrInputDocument();

             solrDoc.addField("id", MessageUtil.md5(document.getDocName()));
             solrDoc.addField("docName", document.getDocName());
             solrDoc.addField("revision", document.getRevision());
             solrDoc.addField("author", document.getLastModifyAuthor());
             solrDoc.addField("lastModifyTime", document.getLastModifyTime());
             solrDoc.addField("svnUrl", document.getSvnUrl());
             solrDoc.addField("content", document.getContent());

             solrClient.add(solrDoc);
             solrClient.commit();
         }catch(Exception e){
             logger.error(e.getMessage(), e);
         }

    }

    public List<SVNDocument> searchSolrDocment(String keyword) throws IOException, SolrServerException {

        List<SVNDocument> docs = new ArrayList<SVNDocument>();

        SolrQuery query = new SolrQuery("_text_:" + keyword);
        query.setFields("revision","svnUrl","docName", "lastModifyTime", "author");
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
            d.setSvnUrl(doc.getFieldValue("svnUrl").toString());
            d.setDocName(doc.getFieldValue("docName").toString());
            d.setLastModifyAuthor(doc.getFieldValue("author").toString());
            docs.add(d);
        }
        return docs;
    }
}
