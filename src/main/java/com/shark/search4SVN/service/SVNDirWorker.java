package com.shark.search4SVN.service;

import com.shark.search4SVN.util.Constants;
import com.shark.search4SVN.util.JedisAdapter;
import com.shark.search4SVN.util.Search4SVNContext;
import com.shark.search4SVN.util.ThreadUtls;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNNodeKind;

import java.util.List;

/**
 * Created by liuqinghua on 16-9-10.
 */
public class SVNDirWorker implements Runnable {

    private static Logger logger = Logger.getLogger(SVNDirWorker.class);


    private JedisAdapter jedisAdapter;

    private SVNService svnService = null;

    public SVNDirWorker(SVNService svnService){
        this.svnService = svnService;
        this.jedisAdapter = (JedisAdapter) Search4SVNContext.getBean(JedisAdapter.class);
    }

    @Override
    public void run() {

         while(!ScheduleService.isStop()){
             ThreadUtls.sleep(2000);

             String url = jedisAdapter.spop(Constants.SVNDIRKEY);
             if(StringUtils.isEmpty(url)){
                 continue;
             }

             List<SVNDirEntry> entrys =  svnService.listFolder(url);

             if(entrys != null){
                 for(SVNDirEntry entry:entrys){
                     if(SVNNodeKind.DIR.equals(entry.getKind())){

                         String newUrl = url + "/" + entry.getRelativePath();
                         jedisAdapter.sadd(Constants.SVNDIRKEY, newUrl);
                     }else{

                         String newUrl = url + "/" + entry.getRelativePath();
                         jedisAdapter.sadd(Constants.SVNFILE, newUrl);
                     }
                 }
             }

             jedisAdapter.sadd(Constants.FINISHEDURL, url);
         }
    }
}
