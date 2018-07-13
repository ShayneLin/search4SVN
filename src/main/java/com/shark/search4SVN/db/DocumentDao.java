package com.shark.search4SVN.db;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author lcs
 * @Description 执行数据库操作
 * @Date Created in 17:54 2018/6/14
 */
@Component
public class DocumentDao {

    private static final String TABLE_NAME = "t_document";//表名
    private static final String INSERT_FIELD = "entity_flag,name,doc_url,modify_time,description";//插入的字段
    private static final String SELECT_FIELD = "id,"+INSERT_FIELD;//查询的字段

    public static void main(String[] args) throws SQLException {
        String initSQL = "create table t_document(" +
                "    id int primary key auto_increment," +
                "    entity_flag char(32) unique not null," +
                "    name varchar(128)," +
                "    doc_url varchar(612)," +
                "    modify_time datetime," +
                "    description varchar(128)" +
                " );";
        Document document = new Document();
        document.setName("http://loclhost:8080/test.doc");
        document.setDocUrl("/loclhost:8080/test.doc");
        document.setEntityFlag(Md5Util.md5(document.getDocUrl()));
        document.setModifyTime(new Date());
        document.setDescription("demo");
//        new  DocumentDao().insert(document);
        JdbcConnectionPoolHelper.getConnection().executeUpdate(initSQL);
//        String selectSQL = "select doc.* from t_document doc ";
//        ConnectionAdapter connectionAdapter = JdbcConnectionPoolHelper.getConnection();
//        List<Document> documents = connectionAdapter.executeQuery(selectSQL);

    }

    /**
     * 插入一条Document记录
     * @param document
     * @return
     */
    public int insert(Document document) throws SQLException {
        String dataStr = null;
        String replaceStr = "' '";
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = null;
        if (!StringUtils.isEmpty(document.getDescription())) {
            System.out.println("时间："+dateFormatter.format(document.getModifyTime()));
            dataStr = replaceStr.replace(" ", document.getEntityFlag()) + "," +
                    replaceStr.replace(" ", document.getName()) + "," +
                    replaceStr.replace(" ", document.getDocUrl()) + "," +
                    replaceStr.replace(" ", dateFormatter.format(document.getModifyTime()))+","+
                    replaceStr.replace(" ", document.getDescription());
            sql = "insert into " + TABLE_NAME
                    + "(" + INSERT_FIELD + ")" + " values (" +
                    dataStr + ")";
        }else {
            dataStr = replaceStr.replace(" ",document.getEntityFlag())+","+
                    replaceStr.replace(" ",document.getName())+","+
                    replaceStr.replace(" ",document.getDocUrl())+","+
                    replaceStr.replace(" ", dateFormatter.format(document.getModifyTime()));
            sql = "insert into " + TABLE_NAME
                    + "(" + INSERT_FIELD.substring(0, INSERT_FIELD.lastIndexOf(',')) + ")" + " values (" +
                    dataStr + ")";
        }
        System.out.println(sql);
        ConnectionAdapter connectionAdapter = JdbcConnectionPoolHelper.getConnection();
        return  connectionAdapter.executeUpdate(sql);
    }
    public int insert(String sql) throws SQLException {
        ConnectionAdapter connectionAdapter = JdbcConnectionPoolHelper.getConnection();
        return  connectionAdapter.executeUpdate(sql);
    }

    /**
     * 根据实体获取Document
     * @param entityFlag
     * @return
     * @throws SQLException
     */
    public Document getDocumentByEntityFlag(String entityFlag) throws SQLException {
        String replaceStr = "' '";
        String sql = "select "+SELECT_FIELD+" from " +TABLE_NAME+"where entity_flag ="+replaceStr.replace(" ",entityFlag);
        ConnectionAdapter connectionAdapter = JdbcConnectionPoolHelper.getConnection();
        return  connectionAdapter.selectOne(sql);
    }
}
