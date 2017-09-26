package com.shark.search4SVN.service.disruptor;

import com.lmax.disruptor.EventHandler;
import com.shark.search4SVN.service.disruptor.event.SVNEvent;
import com.shark.search4SVN.service.wrapper.SolrAdapter;
import com.shark.search4SVN.util.ThreadManager;
import com.shark.search4SVN.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by qinghualiu on 2017/5/21.
 */
public class DisruptorSolrWorker implements EventHandler<SVNEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DisruptorSolrWorker.class);

    private SolrAdapter solrAdapter;

    @Override
    public void onEvent(SVNEvent event, long sequence, boolean endOfBatch) throws Exception {
        try {
            if (event.getType() == 3) {
                ThreadUtils.sleep(500);
                solrAdapter.addSolrDocument(event.getDocument());
              /*  ThreadManager.getInstance().submitTask(new Runnable() {
                    @Override
                    public void run() {
                        solrAdapter.addSolrDocument(event.getDocument());
                    }
                });*/
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    public void setSolrAdapter(SolrAdapter solrAdapter) {
        this.solrAdapter = solrAdapter;
    }
}
