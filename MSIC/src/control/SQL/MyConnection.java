package control.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    public Connection connection;
    String sqlServer;
    String hostname;
    int port;
    String SID;
    String user;
    String password;
    
    public Connection getConnection()
    {
        try
        {
            Class.forName(sqlServer);
        }
        catch(ClassNotFoundException e)
        {	
            System.out.println("Driver-ul MySql nu a fost gasit");
        }
        connection = null;
        try
        {
            String select = "jdbc:oracle:thin:@" + hostname + ":" + port + ":" + SID;
            connection = DriverManager.getConnection(select, user, password);
        }
        catch(SQLException e)
        {
            System.out.println("Nu exista baza de date sau localhost-ul");
        }
        return connection;
    }
}

