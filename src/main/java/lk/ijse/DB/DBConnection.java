package lk.ijse.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static DBConnection dbConnection;

    private final static String URL ="jdbc:mysql://localhost:3306/go_fish";
    private final static Properties props = new Properties();

    private Connection connection;

    {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }

    private DBConnection() throws SQLException {
        connection = DriverManager.getConnection(URL, props);
    }

    public static DBConnection getInstance() throws SQLException {
        if(dbConnection == null){
            return dbConnection = new DBConnection();
        }else{
            return dbConnection;
        }
    }

    public Connection getConnection(){
        return connection;
    }
}
