package com.shark.search4SVN.util;

import com.shark.search4SVN.pojo.SVNDocument;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuqinghua on 16-9-13.
 */
@Component
@Scope("prototype")
public class SolrAdapter implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SolrAdapter.class);
    private SolrClient solrClient;

    @Autowired
    private SolrConnProperties solrConnProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        //String url = solrProperties.url;

        //solrClient = new HttpSolrClient.Builder(url).build();
    }

    public void addSolrDocument(SVNDocument document){
         SolrInputDocument solrDoc = null;
         try{
             solrDoc = new SolrInputDocument();
             solrDoc.addField("docName", document.getDocName());
             solrDoc.addField("revision", document.getRevision());
             solrDoc.addField("author", document.getLastModifyAuthor());
             solrDoc.addField("lastModifyTime", document.getLastModifyTime());
             solrDoc.addField("svnUrl", document.getSvnUrl());
             solrDoc.addField("content", document.getContent());

             solrClient.add(solrDoc);
         }catch(Exception e){
             logger.error(e.getMessage(), e);
         }

    }

    public List<SVNDocument> searchSolrDocment(String keyword) throws IOException, SolrServerException {

        List<SVNDocument> docs = new ArrayList<SVNDocument>();

        SolrQuery query = new SolrQuery(keyword);
        query.setFields("revison","svnurl","docName", "lastUpdateTime");
        //query.setSort("lastUpdateTime", ORDER.desc);
        query.setStart(0);
        query.setRows(50);
        QueryResponse response = solrClient.query(query);

        // 搜索得到的结果数
        //System.out.println("Find:" + response.getResults().getNumFound());
        // 输出结果
        for (SolrDocument doc : response.getResults()) {
            SVNDocument d = new SVNDocument();
            d.setRevision(doc.getFieldValue("revison").toString());
            d.setSvnUrl(doc.getFieldValue("svnurl").toString());
            d.setDocName(doc.getFieldValue("docName").toString());
            docs.add(d);
        }
        return docs;
    }

    public SolrConnProperties getSolrConnProperties() {
        return solrConnProperties;
    }

    public void setSolrConnProperties(SolrConnProperties solrConnProperties) {
        this.solrConnProperties = solrConnProperties;
    }
}
