import org.h2.jdbc.JdbcConnection;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * @Author linchangshun
 * @Description
 * @Date Created in 16:56 2018/6/14
 */

public class TestH2 {
    String dbPath ="db/searchSVN";
    JdbcConnectionPool jdbcConnection;
    @Before
    public void init(){
        jdbcConnection = JdbcConnectionPool.create("jdbc:h2:" + dbPath, "sa", "");
        jdbcConnection.setMaxConnections(30);
    }


    public void createH2Database() throws SQLException {
        JdbcConnection connection = (JdbcConnection) jdbcConnection.getConnection();
    }

    public void createTable() throws SQLException {
        JdbcConnection connection = (JdbcConnection) jdbcConnection.getConnection();
        Statement statement = connection.createStatement();
        String create_sql = "create table user(id int auto_increment primary key,name varchar(64),age int(3));";
        statement.execute(create_sql);
        connection.commit();
    }


    public void insertToTable()throws SQLException{
        JdbcConnection connection = (JdbcConnection)jdbcConnection.getConnection();
        Statement statement = connection.createStatement();
        for (int i=0;i < 10;i++){
            String insert_sql = "insert into user(name,age) values ('user',20);";
            statement.execute(insert_sql);

        }
        connection.commit();
    }

    @Test
    public void queryFromTable() throws SQLException {
        JdbcConnection connection = (JdbcConnection)jdbcConnection.getConnection();
        Statement statement = connection.createStatement();
        String query_sql = "select * from user;";
        ResultSet rs = statement.executeQuery(query_sql);
        while (rs.next()){
            System.out.println(rs.getString(2));
        }
    }

}
