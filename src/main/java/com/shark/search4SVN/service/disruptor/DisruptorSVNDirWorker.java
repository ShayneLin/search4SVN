package com.shark.search4SVN.service.disruptor;

import com.lmax.disruptor.EventHandler;
import com.shark.search4SVN.service.redis.SVNService;
import com.shark.search4SVN.service.wrapper.SVNAdapter;
import com.shark.search4SVN.service.disruptor.event.SVNEvent;
import com.shark.search4SVN.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNNodeKind;

import java.util.List;

/**
 * Created by qinghualiu on 2017/5/21.
 */
public class DisruptorSVNDirWorker implements EventHandler<SVNEvent> {
    private static final Logger logger = LoggerFactory.getLogger(DisruptorSVNDirWorker.class);

    private SVNService svnService;

    private DisruptorScheduleService disruptorScheduleService;

    @Override
    public void onEvent(SVNEvent event, long sequence, boolean endOfBatch) throws Exception {
        try {
            if (event.getType() == 1) {
                ThreadUtils.sleep(500);
                String url = event.getUrl();

                String key = event.getSvnKey();

                logger.info("处理 url " + url);

                List<SVNDirEntry> entrys = svnService.getSVNAdapter(key).listFolder(url);

                if (entrys != null) {
                    for (SVNDirEntry entry : entrys) {
                        if (SVNNodeKind.DIR.equals(entry.getKind())) {

                            String newUrl = url + "/" + entry.getRelativePath();
                            //disruptor publish new event with newUrl type 1;
                            disruptorScheduleService.produceEvent(1, newUrl, key, null);
                        } else {

                            String newUrl = url + "/" + entry.getRelativePath();
                            //disruptor publish new event with newUrl type 2;
                            disruptorScheduleService.produceEvent(2, newUrl, key, null);
                        }
                    }
                }
                disruptorScheduleService.addHandled(url);
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    public void setSvnService(SVNService svnService) {
        this.svnService = svnService;
    }

    public void setDisruptorScheduleService(DisruptorScheduleService disruptorScheduleService) {
        this.disruptorScheduleService = disruptorScheduleService;
    }
}
