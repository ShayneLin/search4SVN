package com.shark.search4SVN.service.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.shark.search4SVN.pojo.HandledContent;
import com.shark.search4SVN.pojo.SVNDocument;
import com.shark.search4SVN.service.disruptor.event.SVNEvent;
import com.shark.search4SVN.service.disruptor.event.SVNEventFactory;
import com.shark.search4SVN.service.SVNService;
import com.shark.search4SVN.util.DisruptorProperties;
import com.shark.search4SVN.service.wrapper.SolrAdapter;
import com.shark.search4SVN.util.EventConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by qinghualiu on 2017/5/21.
 */
@Service
public class DisruptorScheduleService implements InitializingBean, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(DisruptorScheduleService.class);

    @Autowired
    private DisruptorProperties disruptorProperties = null;

    private ApplicationContext applicationContext;

    private RingBuffer<SVNEvent> ringBuffer = null;

    private HandledContent handledContent;

    @Override
    public void afterPropertiesSet() throws Exception {
        //int bufferSize = disruptorProperties.getSize();
        int bufferSize = 4096*4;
        logger.info(String.format("启动 disruptor, bufferSize 大小为 %d", bufferSize));
        SVNEventFactory factory = new SVNEventFactory();

        Disruptor<SVNEvent> disruptor = new Disruptor<SVNEvent>(factory, bufferSize,
                Executors.defaultThreadFactory());

        DisruptorSolrWorker disruptorSolrWorker = new DisruptorSolrWorker();
        DisruptorSVNWorker disruptorSVNWorker = new DisruptorSVNWorker();

        SVNService svnService = (SVNService)applicationContext.getBean("svnService");
        SolrAdapter solrAdapter = (SolrAdapter) applicationContext.getBean("solrAdapter");

        disruptorSVNWorker.setSvnService(svnService);
        disruptorSVNWorker.setDisruptorScheduleService(this);
        disruptorSolrWorker.setSolrAdapter(solrAdapter);

        disruptor.handleEventsWith(disruptorSVNWorker, disruptorSolrWorker);

        this.handledContent = new HandledContent();
        this.ringBuffer = disruptor.getRingBuffer();
        disruptor.start();
        logger.info("disruptor 启动成功");
    }

    public void produceEvent(int type, String url, String key, SVNDocument svnDocument){
        long sequence = ringBuffer.next();  // Grab the next sequence
        try {
            SVNEvent event = ringBuffer.get(sequence); // Get the entry in the Disruptor
            event.setType(type);
            if(type == EventConstants.SVNEVENT){
                event.setUrl(url);
                event.setSvnKey(key);
            }else if(type == EventConstants.SOLREVENT){
                event.setDocument(svnDocument);
            }
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    public void addHandled(String url){
        this.handledContent.addHandled(url);
    }

    public List<String> getHandled(){
        return this.handledContent.getHandled();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
