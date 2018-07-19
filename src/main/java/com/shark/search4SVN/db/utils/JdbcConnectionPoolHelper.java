package com.shark.search4SVN.db.utils;

import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;

/**
 * @Author lcs
 * @Description  连接辅助类类
 * @Date Created in 17:28 2018/6/14
 */
public class JdbcConnectionPoolHelper {
    private static final Logger logger = LoggerFactory.getLogger(JdbcConnectionPoolHelper.class);
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

    /**
     * 从连接池里取连接,封装过的Connection
     * @return
     * @throws SQLException
     */
    public static ConnectionExecutorAdapter  getConnection() throws SQLException {
        if (connectionPool.getMaxConnections() == connectionPool.getActiveConnections()){
            logger.info("没有空余的连接了");
            return  null;
        }
        return new ConnectionExecutorAdapter(connectionPool.getConnection());
    }
}
