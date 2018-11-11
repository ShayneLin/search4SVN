package com.shark.search4SVN.service.disruptor;

import com.lmax.disruptor.EventHandler;
import com.shark.search4SVN.db.DocumentDao;
import com.shark.search4SVN.db.utils.Md5Util;
import com.shark.search4SVN.pojo.Document;
import com.shark.search4SVN.pojo.SVNDocument;
import com.shark.search4SVN.service.DocumentService;
import com.shark.search4SVN.service.SVNService;
import com.shark.search4SVN.service.wrapper.SVNAdapter;
import com.shark.search4SVN.service.disruptor.event.SVNEvent;
import com.shark.search4SVN.util.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNNodeKind;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qinghualiu on 2017/5/21.
 */
public class DisruptorSVNWorker implements EventHandler<SVNEvent> {
    private static final Logger logger = LoggerFactory.getLogger(DisruptorSVNWorker.class);

    private SVNService svnService;

    private DocumentService documentService;

    private DisruptorScheduleService disruptorScheduleService;

    @Override
    public void onEvent(SVNEvent event, long sequence, boolean endOfBatch) throws Exception {
        try {
            if (event.getType() == EventConstants.SVNEVENT) {
                ThreadUtils.sleep(100);
                String url = event.getUrl();

                disruptorScheduleService.addHandled(url);

                String key = event.getSvnKey();

                logger.info("处理 url " + url);
                SVNAdapter svnAdapter = svnService.getSVNAdapter(key);

                int result = svnAdapter.checkPath(url);
                if(result == FileTypeConstants.DIRTYPE) {
                    logger.info("处理目录： url " + url);
                    List<SVNDirEntry> entrys = svnAdapter.listFolder(url);

                    if (entrys != null) {
                        for (SVNDirEntry entry : entrys) {
                            if (SVNNodeKind.DIR.equals(entry.getKind())) {

                                String newUrl = url + "/" + entry.getRelativePath();
                                //disruptor publish new event with newUrl type 1;
                                logger.info("处理目录： url " + newUrl);
                                disruptorScheduleService.produceEvent(EventConstants.SVNEVENT, newUrl, key, null);
                            } else {

                               /* String newUrl = url + "/" + entry.getRelativePath();
                                //disruptor publish new event with newUrl type 2;
                                disruptorScheduleService.produceEvent(2, newUrl, key, null);*/
                                String newUrl = url + "/" + entry.getRelativePath();
                                logger.info("处理文件： url " + newUrl);
                                parseDocContent(svnAdapter, newUrl);

                            }
                        }
                    }
                }else if(result == FileTypeConstants.FILETYPE){
                    //disruptorScheduleService.produceEvent(2, url, key, null);
                    logger.info("处理文件： url " + url);
                    parseDocContent(svnAdapter, url);
                }

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
                    String docName = FilenameUtils.getName(url);
                    logger.info("处理文档名称： name " + docName);

                    if(!DisruptorSVNWorker.this.accept(docName, mimeType)){//只接受指定的文件格式的文件，其他的不予理会
                        logger.info("不接收 文档名称： name " + docName);
                        return;
                    }


                    String text = null;
                    try {
                        text = tika.parseToString(new ByteArrayInputStream(bytes));
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    } catch (TikaException e) {
                        logger.error(e.getMessage(), e);
                    }
                    text = StringUtils.trimWhitespace(text);


                    try {
                        SVNDocument document = new SVNDocument();
                        document.setDocName(docName);
                        document.setRevision(String.valueOf(entry.getRevision()));
                        document.setSvnUrl(url);
                        document.setLastModifyAuthor(entry.getAuthor());
                        document.setLastModifyTime(entry.getDate());
                        document.setContent(text);
                        document.setMimeType(mimeType);

                        disruptorScheduleService.produceEvent(EventConstants.SOLREVENT, null, null, document);
                        //记录拉取的文档，判断是否将该文档记录到数据库表中
                        Document toPersitDocument = new Document();
                        Date date = entry.getDate();
                        String docUrl = entry.getURL().toString();
                        toPersitDocument.setEntityFlag(Md5Util.md5(docUrl));
                        toPersitDocument.setName(docName);
                        toPersitDocument.setDocUrl(docUrl);
                        toPersitDocument.setModifyTime(date);
                        DocumentDao documentDao = new DocumentDao();
//                        DocumentService documentService = new DocumentService();
                        DocumentService documentService = (DocumentService) ApplicationContextUtil.getInstace().getBean("documentService");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        //调用插入或者更新文档记录
//                    Document isExistRecord = documentDao.getDocumentByEntityFlag(toPersitDocument.getEntityFlag());
                        Document isExistRecord = documentService.getDocumentByEntityFlag(toPersitDocument.getEntityFlag());
                        String dateStr = dateFormat.format(date);
                        if (isExistRecord != null){
                            //TODO:当该文档已存在数据库中
                            String existDateStr = dateFormat.format(isExistRecord.getModifyTime());
                            if(dateStr.equals(existDateStr))//使用该方式比较时间原因是，getTime()比较的是毫秒，所以即使时分秒相等，两个Date也未必一样。
                                return;
                            //时间不一致，说明有修改,进行更新
                            Map<String,Object> toUpdateDataMap = new HashMap<>();
                            toUpdateDataMap.put("modify_time",dateStr);
                            documentService.updateDocument(toUpdateDataMap,toPersitDocument.getEntityFlag());
                            logger.info("往数据库更新了："+docUrl);
                            return;

                        }
//                    documentDao.insert(toPersitDocument);
                        documentService.insert(toPersitDocument);
                        logger.info("往数据库中记录了："+docUrl);
                    } catch (Exception e) {
                        logger.info(e.getMessage(),e);
                    }
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

    /**
     * 判断是否分析从SVN上获取的文件内容
     * TODO 当前先根据 后缀名来处理，今后考虑从mimeType上判断
     * @param docName
     * @param mimeType
     * @return true 接受文件，false，不接受
     */
    private boolean accept(String docName, String mimeType) {
        String extName = FilenameUtils.getExtension(docName);
        logger.info("文档名称： name-extName: " + docName + " - " + extName);
        if(!StringUtils.isEmpty(extName)){
            if("txt".equalsIgnoreCase(extName)
               || "doc".equalsIgnoreCase(extName)
                    || "docx".equalsIgnoreCase(extName)
                    || "ppt".equalsIgnoreCase(extName)
                    || "xls".equalsIgnoreCase(extName)
                    || "xlsx".equalsIgnoreCase(extName)
                    || "java".equalsIgnoreCase(extName)
                    || "sql".equalsIgnoreCase(extName)){
                return true;
            }
        }

        return false;
    }
}
