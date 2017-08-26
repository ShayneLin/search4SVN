package com.shark.search4SVN.service.disruptor;

import com.lmax.disruptor.EventHandler;
import com.shark.search4SVN.pojo.SVNDocument;
import com.shark.search4SVN.service.SVNService;
import com.shark.search4SVN.service.disruptor.event.SVNEvent;
import com.shark.search4SVN.util.Constants;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.tmatesoft.svn.core.SVNDirEntry;

import java.io.ByteArrayInputStream;

/**
 * Created by qinghualiu on 2017/5/21.
 */
public class DisruptorSVNFileWorker implements EventHandler<SVNEvent> {

    private SVNService svnService;
    private DisruptorScheduleService disruptorScheduleService;

    @Override
    public void onEvent(SVNEvent event, long sequence, boolean endOfBatch) throws Exception {
        if(event.getType() == 2){
            String url = event.getUrl();

            Object[] objects = svnService.checkoutFile(url);

            if(objects == null || objects.length != 2){
                throw new Exception("svnService.checkoutFile error");
            }

            SVNDirEntry entry = (SVNDirEntry) objects[0];
            byte[] bytes = (byte[]) objects[1];

            Tika tika = new Tika();

            String mimeType = tika.detect(bytes);

            String text = tika.parseToString(new ByteArrayInputStream(bytes));
            text = StringUtils.trimWhitespace(text);

            SVNDocument document = new SVNDocument();
            document.setDocName(entry.getName());
            document.setRevision(String.valueOf(entry.getRevision()));
            document.setSvnUrl(url);
            document.setLastModifyAuthor(entry.getAuthor());
            document.setLastModifyTime(entry.getDate());
            document.setContent(text);
            document.setMimeType(mimeType);

            disruptorScheduleService.produceEvent(3,null, document);
            disruptorScheduleService.addHandled(url);
        }
    }

    public void setSvnService(SVNService svnService) {
        this.svnService = svnService;
    }

    public void setDisruptorScheduleService(DisruptorScheduleService disruptorScheduleService) {
        this.disruptorScheduleService = disruptorScheduleService;
    }
}
