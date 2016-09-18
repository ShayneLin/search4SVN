package com.shark.search4SVN.service;

import com.shark.search4SVN.pojo.SVNDocument;
import com.shark.search4SVN.util.SolrAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuqinghua on 16-9-18.
 */
@Service
public class SearchService {
    @Autowired
    private SolrAdapter solrAdapter;


    public List<SVNDocument> searchByKey(String searchKey){
        return null;
    }

    public void setSolrAdapter(SolrAdapter solrAdapter) {
        this.solrAdapter = solrAdapter;
    }
}
