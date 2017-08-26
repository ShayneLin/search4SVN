package com.shark.search4SVN.service.disruptor;

import com.lmax.disruptor.EventHandler;
import com.shark.search4SVN.service.SVNService;
import com.shark.search4SVN.service.disruptor.event.SVNEvent;
import com.shark.search4SVN.util.Constants;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNNodeKind;

import java.util.List;

/**
 * Created by qinghualiu on 2017/5/21.
 */
public class DisruptorSVNDirWorker implements EventHandler<SVNEvent> {

    private SVNService svnService;
    private DisruptorScheduleService disruptorScheduleService;

    @Override
    public void onEvent(SVNEvent event, long sequence, boolean endOfBatch) throws Exception {
        if(event.getType() == 1){
            String url = event.getUrl();
            List<SVNDirEntry> entrys =  svnService.listFolder(url);

            if(entrys != null){
                for(SVNDirEntry entry:entrys){
                    if(SVNNodeKind.DIR.equals(entry.getKind())){

                        String newUrl = url + "/" + entry.getRelativePath();
                        //TODO disruptor publish new event with newUrl type 1;
                        disruptorScheduleService.produceEvent(1, newUrl, null);
                    }else{

                        String newUrl = url + "/" + entry.getRelativePath();
                        //TODO disruptor publish new event with newUrl type 2;
                        disruptorScheduleService.produceEvent(2, newUrl, null);
                    }
                }
            }
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
