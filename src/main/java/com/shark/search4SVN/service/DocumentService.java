package com.shark.search4SVN.service;

import com.shark.search4SVN.dao.DocumentDao;
import com.shark.search4SVN.db.utils.ConnectionExecutorAdapter;
import com.shark.search4SVN.db.utils.JdbcConnectionPoolHelper;
import com.shark.search4SVN.pojo.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Map;

@Service
public class DocumentService {

    @Autowired
    private DocumentDao documentDao;

    public int insert(Document record){
        return  documentDao.insert(record);
    }


    public int updateDocument(Map<String,Object> params,String entityFlag){
        return documentDao.updateDocument(params,entityFlag);
    }

    /**
     * 根据实体获取Document
     * @param entityFlag
     * @return
     * @throws SQLException
     */
    public Document getDocumentByEntityFlag(String entityFlag) {
        return documentDao.getDocumentByEntityFlag(entityFlag);
    }
}
