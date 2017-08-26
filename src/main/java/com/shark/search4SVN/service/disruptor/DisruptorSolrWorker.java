package com.shark.search4SVN.service.disruptor;

import com.lmax.disruptor.EventHandler;
import com.shark.search4SVN.service.disruptor.event.SVNEvent;
import com.shark.search4SVN.util.SolrAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by qinghualiu on 2017/5/21.
 */
public class DisruptorSolrWorker implements EventHandler<SVNEvent> {

    private SolrAdapter solrAdapter;

    @Override
    public void onEvent(SVNEvent event, long sequence, boolean endOfBatch) throws Exception {
        if(event.getType() == 3){
            solrAdapter.addSolrDocument(event.getDocument());
        }
    }

    public void setSolrAdapter(SolrAdapter solrAdapter) {
        this.solrAdapter = solrAdapter;
    }
}
