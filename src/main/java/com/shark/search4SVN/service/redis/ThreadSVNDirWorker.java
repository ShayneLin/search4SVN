package com.shark.search4SVN.service.redis;

import com.shark.search4SVN.service.SVNService;
import com.shark.search4SVN.util.Constants;
import com.shark.search4SVN.util.JedisAdapter;
import com.shark.search4SVN.util.Search4SVNContext;
import com.shark.search4SVN.util.ThreadUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNNodeKind;

import java.util.List;

/**
 * Created by liuqinghua on 16-9-10.
 */
@Deprecated
public class ThreadSVNDirWorker implements Runnable {

    private static Logger logger = Logger.getLogger(ThreadSVNDirWorker.class);


    private JedisAdapter jedisAdapter;

    private SVNService svnService = null;

    public ThreadSVNDirWorker(SVNService svnService){
        this.svnService = svnService;
        this.jedisAdapter = (JedisAdapter) Search4SVNContext.getBean(JedisAdapter.class);
    }

    @Override
    public void run() {

         while(!ThreadScheduleService.isStop()){
             ThreadUtils.sleep(2000);

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
