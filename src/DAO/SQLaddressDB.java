package DAO;

import static DAO.DBconnection.conn;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.Address;
import static view_controller.LoginController.loginUser;

/**
 *
 * @author Kaleb Chatland
*/ 
public class SQLaddressDB
{
    public static int getAddressID(String address) 
    {
        int addressID = 0;
        String getAddressSQL = "SELECT addressId FROM address WHERE address = ?";
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(getAddressSQL);
            stmt.setString(1, address);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) 
            {
                addressID = rs.getInt("addressId");
            }
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
      return addressID;
    }
    
    public static Address getAddressByID(int addressID) 
    {
        Address addressByID = new Address();
        String getAddressByIDSQL = "SELECT * FROM address WHERE addressId = ?";
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(getAddressByIDSQL);
            stmt.setInt(1, addressID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) 
            {
                addressByID.setAddressID(rs.getInt("addressId"));
                addressByID.setPhone(rs.getString("phone"));
                addressByID.setAddress1(rs.getString("address"));
                addressByID.setAddress2(rs.getString("address2"));
                addressByID.setCity(SQLcityDB.getCityByID(rs.getInt("cityId")));
                addressByID.setPostalCode(rs.getString("postalCode"));    
            }else 
                {
                    return null;
                }
        }catch (SQLException e) 
            {
                e.printStackTrace(); 
            }
      return addressByID;
    }
    
    private static int getMaxAddressID() 
    {
        int maxAddressID = 0;
        String maxAddressIdSQL = "SELECT MAX(addressId) FROM address";
        try 
        {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(maxAddressIdSQL);
            if (rs.next()) 
            {
                maxAddressID = rs.getInt(1);
            }
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
      return maxAddressID + 1;
    }
    
    public static int addAddress(Address address) 
    {
        int addressID = getMaxAddressID();
        String addAddressSQL = String.join(" ",
                "INSERT INTO address (addressId, address, address2, cityId, postalCode, "
                        + "phone, createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?)");     
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(addAddressSQL);
            stmt.setInt(1, addressID);
            stmt.setString(2, address.getAddress1());
            stmt.setString(3, address.getAddress2());
            stmt.setInt(4, address.getCity().getCityID());
            stmt.setString(5, address.getPostalCode());
            stmt.setString(6, address.getPhone());
            stmt.setString(7, loginUser.getUserName());
            stmt.setString(8, loginUser.getUserName());
            stmt.executeUpdate();
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
      return addressID;
    }
    
    public static void updateAddress(Address address) 
    {
        String updateAddressSQL = String.join(" ",
                "UPDATE address",
                "SET address=?, address2=?, cityId=?, postalCode=?, phone=?, lastUpdate=NOW(), lastUpdateBy=?",
                "WHERE addressId=?");
        try
        {
            PreparedStatement stmt = conn.prepareStatement(updateAddressSQL);
            stmt.setString(1, address.getAddress1());
            stmt.setString(2, address.getAddress2());
            stmt.setInt(3, address.getCity().getCityID());
            stmt.setString(4, address.getPostalCode());
            stmt.setString(5, address.getPhone());
            stmt.setString(6, loginUser.getUserName());
            stmt.setInt(7, address.getAddressID());
            stmt.executeUpdate();
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
    }
    
    public void deleteAddress(Address address) 
    {
        String deleteAddressSQL = "DELETE FROM address WHERE addressId = ?";
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(deleteAddressSQL);
            stmt.setInt(1, address.getAddressID());
            stmt.executeUpdate();
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
    }
       
}
