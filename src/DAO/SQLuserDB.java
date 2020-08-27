package DAO;

import static DAO.DBconnection.conn;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

/**
 *
 * @author Kaleb Chatland
 */
public class SQLuserDB
{
    public static ObservableList<User> getAllUsers() 
    {
        ObservableList<User> allUsers = FXCollections.observableArrayList();
        String getAllUsers = "SELECT * FROM user";
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(getAllUsers);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                User user = new User();
                user.setUserID(rs.getInt("userId"));
                user.setUserName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                
                allUsers.add(user);
            }
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
      return allUsers;
    }
    
    public static User getUserByID(int userID) 
    {
        String getUserByIDSQL = "SELECT * FROM user WHERE userId=?";
        User user = new User();
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(getUserByIDSQL);
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) 
            {
                user.setUserID(rs.getInt("userId"));
                user.setUserName(rs.getString("userName"));
            }
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
      return user;
    }
    
    public static User login() 
    {
        User user = new User();
        String getUserQuery = "SELECT * FROM user"; //currently only one user in the system
        try
        {
            PreparedStatement stmt = conn.prepareStatement(getUserQuery);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                user.setUserName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                user.setUserID(rs.getInt("userId"));
            } else 
                {
                    return null;
                }
        } catch(SQLException e)
            {
                e.printStackTrace();
            }
        
      return user;
    }
    
}
