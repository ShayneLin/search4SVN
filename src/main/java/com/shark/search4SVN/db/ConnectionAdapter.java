package com.shark.search4SVN.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author lcs
 * @Description 自定义的Connection适配器
 * @Date Created in 18:12 2018/6/26
 */
public class ConnectionAdapter {
    Connection connection = null;

    public ConnectionAdapter(Connection connection) {
        this.connection = connection;

    }

    /**
     * 查询一条记录
     * @param sql
     * @return
     * @throws SQLException
     */
    public Document selectOne(String sql) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            Document document = new Document();
            document.setId(resultSet.getInt("id"));
            document.setEntityFlag(resultSet.getString("entity_flag"));
            document.setName(resultSet.getString("name"));
            document.setDocUrl(resultSet.getString("doc_url"));
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            document.setModifyTime(dateFormatter.parse(resultSet.getString("modify_time")));
            document.setDescription(resultSet.getString("description"));
            return document;
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();

        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (connection != null){
                connection.close();
            }
        }

        return null;
    }
    /**
     * 执行查询
     * @param sql
     * @param destType
     */
    public void executeQuery(String sql,Class destType) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();

        }finally {
            if (connection != null){
                connection.close();
            }
        }
    }

    /**
     * 插入,更新，删除
     * @param sql
     */
    public int executeUpdate(String sql) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            int recordCount = statement.executeUpdate(sql);
            connection.commit();
            return recordCount;
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        }finally {
            if (connection != null){
                connection.close();
            }
        }
        return -1;
    }


    /**
     * 简单查询
     * @param sql
     */
    public List<Document> executeQuery(String sql) throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;
        List<Document> documents = new ArrayList<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            //将结果集转为对象
            while (resultSet.next()){
                Document doc = new Document();
                doc.setId(resultSet.getInt("id"));
                doc.setEntityFlag(resultSet.getString("entity_flag"));
                doc.setName(resultSet.getString("name"));
                doc.setDocUrl(resultSet.getString("doc_url"));
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                doc.setModifyTime(dateFormatter.parse(resultSet.getString("modify_time")));
                doc.setDescription(resultSet.getString("description"));
                documents.add(doc);
                System.out.println(doc);
            }
            connection.commit();
            return documents;
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (connection != null){
                connection.close();
            }
        }
        return null;
    }


}
