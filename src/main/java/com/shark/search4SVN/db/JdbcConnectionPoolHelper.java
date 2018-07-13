package com.shark.search4SVN.db;

import org.h2.jdbc.JdbcConnection;
import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author lcs
 * @Description  连接辅助类类
 * @Date Created in 17:28 2018/6/14
 */
public class JdbcConnectionPoolHelper {
    private static JdbcConnectionPool connectionPool ;
    private static String DB_PATH = "db/searchSVNDB";
    private static String USERNAME = "sa";
    private static String PASSWORD = "";
    private static int MAX_CONNECTION = 30;

    static {
        connectionPool = JdbcConnectionPool.create("jdbc:h2:"+DB_PATH,USERNAME,PASSWORD);
        connectionPool.setMaxConnections(MAX_CONNECTION);
    }

    private JdbcConnectionPoolHelper(){

    }

    public static ConnectionAdapter  getConnection() throws SQLException {
        if (connectionPool.getMaxConnections() == connectionPool.getActiveConnections()){
            System.out.println("没有空余的连接了");
            return  null;
        }
        return new ConnectionAdapter(connectionPool.getConnection());
    }
}
