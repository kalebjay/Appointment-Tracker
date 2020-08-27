package DAO;

import static DAO.DBconnection.conn;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import static view_controller.LoginController.loginUser;

/**
 *
 * @author Kaleb Chatland
 **/
public class SQLcountryDB
{
    public static ObservableList<Country> getAllCountries() 
    {
        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        String getAllcountriesSQL = "SELECT * FROM country";       
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(getAllcountriesSQL);
            ResultSet rs = stmt.executeQuery();          
            while (rs.next()) 
            {
                Country country = new Country();
                country.setCountryID(rs.getInt("countryId"));
                country.setCountryName(rs.getString("country"));
                allCountries.add(country);
            }
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
      return allCountries;
    }
    
    public static Country getCountryByID(int countryID) 
    {
        Country getCountryByID = new Country();
        String getCountryByIDSQL = "SELECT * FROM country WHERE countryId = ?";
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(getCountryByIDSQL);
            stmt.setInt(1, countryID);
            ResultSet rs = stmt.executeQuery();           
            if (rs.next()) 
            {
                getCountryByID.setCountryID(rs.getInt("countryId"));
                getCountryByID.setCountryName(rs.getString("country"));
            }else 
                {
                    return null;
                }
        }catch (SQLException e) 
            {
                e.printStackTrace();
            }
      return getCountryByID;
    }
    
    private static int getMaxCountryID() 
    {
        int maxCountryID = 0;
        String maxCountryIDSQL = "SELECT MAX(countryId) FROM country";      
        try 
        {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(maxCountryIDSQL);           
            if (rs.next()) 
            {
                maxCountryID = rs.getInt(1);
            }
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
      return maxCountryID + 1;
    }
    
    public static int addCountry(Country country) 
    {
        int countryID = getMaxCountryID();
        String addCountrySQL = String.join(" ",
                "INSERT INTO country (countryId, country, createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, NOW(), ?, NOW(), ?)");        
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(addCountrySQL);
            stmt.setInt(1, countryID);
            stmt.setString(2, country.getCountryName());
            stmt.setString(3, loginUser.getUserName());
            stmt.setString(4, loginUser.getUserName());
            stmt.executeUpdate();
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
      return countryID;
    }
    
    public static void updateCountry(Country country) 
    {
        String updateCountrySQL = String.join(" ",
                "UPDATE country",
                "SET country=?, lastUpdate=NOW(), lastUpdateBy=?",
                "WHERE countryId=?");        
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(updateCountrySQL);
            stmt.setString(1, country.getCountryName());
            stmt.setString(2, loginUser.getUserName());
            stmt.setInt(3, country.getCountryID());
            stmt.executeUpdate();
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
    }
    
    public void deleteCountry(Country country) 
    {
        String deleteCountrySQL = "DELETE FROM country WHERE countryId = ?";     
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(deleteCountrySQL);
            stmt.setInt(1, country.getCountryID());
            stmt.executeUpdate();
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
    }
      
}
