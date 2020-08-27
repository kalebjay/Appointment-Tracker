package model;

/**
 *
 * @author Kaleb Chatland
 */
public class User
{
    private int userID;
    private String userName;
    private String password;
    
    public User() 
    {
        
    }
    
    public User(String userName, String password) 
    {
        this.userName = userName;
        this.password = password;
    }
    
    public User(int userID, String userName, String password) 
    {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
    }   
    
    public void setUserID(int userID) 
    {
        this.userID = userID;
    }
    
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }
    
    public void setPassword(String password) 
    {
        this.password = password;
    } 
    
    public int getUserID() 
    {
        return userID;
    }
    
    public String getUserName() 
    {
        return userName;
    }
    
    public String getPassword() 
    {
        return password;
    }
    
    @Override
    public String toString() 
    {
        return getUserName();
    }
}
