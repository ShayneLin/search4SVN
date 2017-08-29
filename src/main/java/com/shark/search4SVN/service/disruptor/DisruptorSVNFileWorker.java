package com.shark.search4SVN.service.disruptor;

import com.lmax.disruptor.EventHandler;
import com.shark.search4SVN.pojo.SVNDocument;
import com.shark.search4SVN.service.redis.SVNService;
import com.shark.search4SVN.service.wrapper.SVNAdapter;
import com.shark.search4SVN.service.disruptor.event.SVNEvent;
import com.shark.search4SVN.util.ThreadManager;
import com.shark.search4SVN.util.ThreadUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.tmatesoft.svn.core.SVNDirEntry;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by qinghualiu on 2017/5/21.
 */
public class DisruptorSVNFileWorker implements EventHandler<SVNEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DisruptorSVNFileWorker.class);

    private SVNService svnService;

    private DisruptorScheduleService disruptorScheduleService;

    @Override
    public void onEvent(SVNEvent event, long sequence, boolean endOfBatch)  {
        try {
            if (event.getType() == 2) {
                ThreadUtils.sleep(500);
                String url = event.getUrl();

                String key = event.getSvnKey();

                logger.info("处理 url " + url);


                Object[] objects = svnService.getSVNAdapter(key).checkoutFile(url);

                if (objects == null || objects.length != 2) {
                    throw new Exception("svnService.checkoutFile error");
                }

                SVNDirEntry entry = (SVNDirEntry) objects[0];
                byte[] bytes = (byte[]) objects[1];

                Tika tika = new Tika();
                ThreadManager.getInstance().submitTask(new Runnable() {
                    @Override
                    public void run() {
                        String mimeType = tika.detect(bytes);

                        String text = null;
                        try {
                            text = tika.parseToString(new ByteArrayInputStream(bytes));
                        } catch (IOException e) {
                            logger.error(e.getMessage(), e);
                        } catch (TikaException e) {
                            logger.error(e.getMessage(), e);
                        }
                        text = StringUtils.trimWhitespace(text);

                        String docName = FilenameUtils.getName(url);

                        SVNDocument document = new SVNDocument();
                        document.setDocName(docName);
                        document.setRevision(String.valueOf(entry.getRevision()));
                        document.setSvnUrl(url);
                        document.setLastModifyAuthor(entry.getAuthor());
                        document.setLastModifyTime(entry.getDate());
                        document.setContent(text);
                        document.setMimeType(mimeType);

                        disruptorScheduleService.produceEvent(3, null, null, document);
                        disruptorScheduleService.addHandled(url);
                    }
                });

            }
        }catch(Exception e){
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
