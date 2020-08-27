package DAO;

import static DAO.DBconnection.conn;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.City;
import static view_controller.LoginController.loginUser;

/**
 *
 * @author Kaleb Chatland
 **/
public class SQLcityDB
{
    public static int getCityID(String city) 
    {
        int cityID = 0;
        String getCitySQL = "SELECT cityId FROM city WHERE city = ?";      
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(getCitySQL);
            stmt.setString(1, city);
            ResultSet rs = stmt.executeQuery();          
            if (rs.next()) 
            {
                cityID = rs.getInt("cityId");
            }
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }       
      return cityID;
    }
    
    public static City getCityByID(int cityID) 
    {
        City cityByID = new City();
        String getCityByIdSQL = "SELECT * FROM city WHERE cityId = ?"; 
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(getCityByIdSQL);
            stmt.setInt(1, cityID);
            ResultSet rs = stmt.executeQuery();            
            if (rs.next()) 
            {
                cityByID.setCityID(rs.getInt("cityId"));
                cityByID.setCityName(rs.getString("city"));
                cityByID.setCountry(SQLcountryDB.getCountryByID(rs.getInt("countryId")));
            }
            else 
                {
                    return null;
                }
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }        
      return cityByID;
    }
    
    private static int getMaxCityID() 
    {
        int maxCityID = 0;
        String maxCityIdSQL = "SELECT MAX(cityId) FROM city";        
        try 
        {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(maxCityIdSQL);           
            if (rs.next()) 
            {
                maxCityID = rs.getInt(1);
            }
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }       
      return maxCityID + 1;
    }

    public static int addCity(City city) 
    {
        int cityID = getMaxCityID();
        String addCitySQL = String.join(" ",
                "INSERT INTO city (cityId, city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, ?, NOW(), ?, NOW(), ?)");            
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(addCitySQL);
            stmt.setInt(1, cityID);
            stmt.setString(2, city.getCityName());
            stmt.setInt(3, city.getCountry().getCountryID());
            stmt.setString(4, loginUser.getUserName());
            stmt.setString(5, loginUser.getUserName());
            stmt.executeUpdate();
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }       
      return cityID;
    }
    
    public static void updateCity(City city) 
    {
        String updateCitySQL = String.join(" ",
                "UPDATE city",
                "SET city=?, countryId=?, lastUpdate=NOW(), lastUpdateBy=?",
                "WHERE cityId=?");     
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(updateCitySQL);
            stmt.setString(1, city.getCityName());
            stmt.setInt(2, city.getCountry().getCountryID());
            stmt.setString(3, loginUser.getUserName());
            stmt.setInt(4, city.getCityID());
            stmt.executeUpdate();
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
    }
    
    public void deleteCity(City city) 
    {
        String deleteCitySQL = "DELETE FROM city WHERE cityId = ?";      
        try {
            PreparedStatement stmt = conn.prepareStatement(deleteCitySQL);
            stmt.setInt(1, city.getCityID());
            stmt.executeUpdate();
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
    }
}
