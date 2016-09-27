package com.shark.search4SVN.service;

import com.alibaba.fastjson.JSONObject;
import com.shark.search4SVN.pojo.SVNDocument;
import com.shark.search4SVN.util.Constants;
import com.shark.search4SVN.util.JedisAdapter;
import com.shark.search4SVN.util.Search4SVNContext;
import com.shark.search4SVN.util.ThreadUtls;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.tmatesoft.svn.core.SVNDirEntry;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by liuqinghua on 16-9-10.
 */
public class SVNFileWorker implements Runnable {

    private static Logger logger = Logger.getLogger(SVNFileWorker.class);

    private JedisAdapter jedisAdapter;

    private SVNService svnService = null;

    public SVNFileWorker(SVNService svnService){
        this.svnService = svnService;
        this.jedisAdapter = (JedisAdapter) Search4SVNContext.getBean(JedisAdapter.class);
    }


    @Override
    public void run() {


        while(!ScheduleService.isStop()) {
            ThreadUtls.sleep(3000);

            try {
                String url = jedisAdapter.spop(Constants.SVNFILE);
                if (StringUtils.isEmpty(url)) {
                    continue;
                }
                /**
                 * 如果不是指定的文件类型的话，则直接跳过
                 */
                if(!url.endsWith("doc") && !url.endsWith("docx") && !url.endsWith("pdf") && !url.endsWith("java")
                        && !url.endsWith("pdf") && !url.endsWith("xlsx") && !url.endsWith("xls") && !url.endsWith("txt")){
                    continue;
                }
                Object[] objects = svnService.checkoutFile(url);

                if(objects == null || objects.length != 2){
                    throw new Exception("svnService.checkoutFile error");
                }

                SVNDirEntry entry = (SVNDirEntry) objects[0];
                byte[] bytes = (byte[]) objects[1];

                Tika tika = new Tika();

                String mimeType = tika.detect(bytes);

                String text = tika.parseToString(new ByteArrayInputStream(bytes));
                text = text.trim();

                SVNDocument document = new SVNDocument();
                document.setDocName(entry.getName());
                document.setRevision(String.valueOf(entry.getRevision()));
                document.setSvnUrl(url);
                document.setLastModifyAuthor(entry.getAuthor());
                document.setLastModifyTime(entry.getDate());
                document.setContent(text);

                String json = JSONObject.toJSONString(document);

                jedisAdapter.sadd(Constants.SOLRDOC, json);
                jedisAdapter.sadd(Constants.FINISHEDURL, url);

            }catch(IOException e){
                logger.error(e.getMessage(), e);
                continue;
            } catch (TikaException e) {
                logger.error(e.getMessage(), e);
                continue;
            }catch (Exception e){
                logger.error(e.getMessage(), e);
                continue;
            }
        }
    }
}
