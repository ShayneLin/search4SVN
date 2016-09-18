package com.shark.search4SVN.util;

import com.shark.search4SVN.pojo.SVNDocument;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by liuqinghua on 16-9-13.
 */
@Component
@Scope("prototype")
public class SolrAdapter implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SolrAdapter.class);
    private SolrClient solrClient;

    @Autowired
    private SolrProperties solrProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        String url = solrProperties.url;

        solrClient = new HttpSolrClient.Builder(url).build();
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





    public void setSolrProperties(SolrProperties solrProperties) {
        this.solrProperties = solrProperties;
    }


    @Component
    @ConfigurationProperties(prefix = "search4SVN.solr", locations = "classpath:config/search4SVN.properties")
    static class SolrProperties{
        private String url;

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
