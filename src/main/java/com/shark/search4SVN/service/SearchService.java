package com.shark.search4SVN.service;

import com.shark.search4SVN.pojo.SVNDocument;
import com.shark.search4SVN.service.wrapper.SolrAdapter;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by liuqinghua on 16-9-18.
 */
@Service
public class SearchService {
    @Autowired
    private SolrAdapter solrAdapter;


    public List<SVNDocument> searchByKey(String searchKey) throws IOException, SolrServerException {
        List<SVNDocument> docs = solrAdapter.searchSolrDocment(searchKey);
        return docs;
    }

    public void setSolrAdapter(SolrAdapter solrAdapter) {
        this.solrAdapter = solrAdapter;
    }
}
