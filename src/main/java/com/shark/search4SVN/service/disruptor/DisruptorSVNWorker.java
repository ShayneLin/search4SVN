package com.shark.search4SVN.service.disruptor;

import com.lmax.disruptor.EventHandler;
import com.shark.search4SVN.pojo.SVNDocument;
import com.shark.search4SVN.service.SVNService;
import com.shark.search4SVN.service.wrapper.SVNAdapter;
import com.shark.search4SVN.service.disruptor.event.SVNEvent;
import com.shark.search4SVN.util.EventConstants;
import com.shark.search4SVN.util.FileTypeConstants;
import com.shark.search4SVN.util.ThreadManager;
import com.shark.search4SVN.util.ThreadUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNNodeKind;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by qinghualiu on 2017/5/21.
 */
public class DisruptorSVNWorker implements EventHandler<SVNEvent> {
    private static final Logger logger = LoggerFactory.getLogger(DisruptorSVNWorker.class);

    private SVNService svnService;

    private DisruptorScheduleService disruptorScheduleService;

    @Override
    public void onEvent(SVNEvent event, long sequence, boolean endOfBatch) throws Exception {
        try {
            if (event.getType() == EventConstants.SVNEVENT) {
                ThreadUtils.sleep(100);
                String url = event.getUrl();

                String key = event.getSvnKey();

                logger.info("处理 url " + url);
                SVNAdapter svnAdapter = svnService.getSVNAdapter(key);

                int result = svnAdapter.checkPath(url);
                if(result == FileTypeConstants.DIRTYPE) {
                    List<SVNDirEntry> entrys = svnAdapter.listFolder(url);

                    if (entrys != null) {
                        for (SVNDirEntry entry : entrys) {
                            if (SVNNodeKind.DIR.equals(entry.getKind())) {

                                String newUrl = url + "/" + entry.getRelativePath();
                                //disruptor publish new event with newUrl type 1;
                                disruptorScheduleService.produceEvent(EventConstants.SVNEVENT, newUrl, key, null);
                            } else {

                               /* String newUrl = url + "/" + entry.getRelativePath();
                                //disruptor publish new event with newUrl type 2;
                                disruptorScheduleService.produceEvent(2, newUrl, key, null);*/
                                parseDocContent(svnAdapter, url);

                            }
                        }
                    }
                }else if(result == FileTypeConstants.FILETYPE){
                    //disruptorScheduleService.produceEvent(2, url, key, null);
                    parseDocContent(svnAdapter, url);
                }
                disruptorScheduleService.addHandled(url);
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    private void parseDocContent(SVNAdapter svnAdapter, String url) {

        ThreadManager.getInstance().submitTask(new Runnable() {
            @Override
            public void run() {
                try {
                    Object[] objects = svnAdapter.checkoutFile(url);

                    if (objects == null || objects.length != 2) {
                        throw new Exception("svnService.checkoutFile error");
                    }

                    SVNDirEntry entry = (SVNDirEntry) objects[0];
                    byte[] bytes = (byte[]) objects[1];

                    Tika tika = new Tika();

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

                    disruptorScheduleService.produceEvent(EventConstants.SOLREVENT, null, null, document);

                }catch (Exception e){
                    logger.error(e.getMessage(), e);
                }
            }
        });

    }

    public void setSvnService(SVNService svnService) {
        this.svnService = svnService;
    }

    public void setDisruptorScheduleService(DisruptorScheduleService disruptorScheduleService) {
        this.disruptorScheduleService = disruptorScheduleService;
    }
}
