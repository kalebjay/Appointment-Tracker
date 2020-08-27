package DAO;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Kaleb Chatland
 */
public class DBconnection
{
    // JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//3.227.166.251/U05VB9";
    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress;
    // Driver Interface reference
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    protected static Connection conn = null;
    
    private static final String userName = "U05VB9"; //user name
    private static final String password = "53688615689"; //password
    
    public static Connection startConnection() throws ClassNotFoundException, SQLException, Exception
    {
        Class.forName(MYSQLJDBCDriver);
        conn = (Connection)DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("Connection Successful");
               
        return conn;
    }
    
    public static void closeConnection()throws ClassNotFoundException, SQLException, Exception
    {
        conn.close();
        System.out.println("Connection Closed!");       
    } 
   
}
