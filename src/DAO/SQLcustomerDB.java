package DAO;

import static DAO.DBconnection.conn;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import static view_controller.LoginController.loginUser;

/**
 *
 * @author Kaleb Chatland
 */
public class SQLcustomerDB
{    
    public static ObservableList<Customer> getAllCustomers() 
    {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        String getAllCustomersSQL = "SELECT * FROM customer WHERE active = 1";      
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(getAllCustomersSQL);
            ResultSet rs = stmt.executeQuery();          
            while (rs.next()) 
            {
                Customer customer = new Customer();
                customer.setCustomerID(rs.getInt("customerId"));
                customer.setCustomerName(rs.getString("customerName"));
                customer.setAddress(SQLaddressDB.getAddressByID(rs.getInt("addressId")));
                allCustomers.add(customer);
            }
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
      return allCustomers;
    }
    
    public static Customer getCustomerByID(int customerID) 
    {
        Customer getCustomerInfo = new Customer();
        String getCustomerByIDSQL = "SELECT * FROM customer WHERE customerId = ?";          
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(getCustomerByIDSQL);
            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();          
            if (rs.next()) 
            {
                getCustomerInfo.setCustomerID(rs.getInt("customerId"));
                getCustomerInfo.setCustomerName(rs.getString("customerName"));
                getCustomerInfo.setAddressID(rs.getInt("addressId"));
                getCustomerInfo.setAddress(SQLaddressDB.getAddressByID(rs.getInt("addressId")));  
            } else 
                {
                    return null;
                }
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
      return getCustomerInfo;
    }
    
    private static int getMaxCustomerID() 
    {
        int maxCustomerID = 0;
        String maxCustomerIDSQL = "SELECT MAX(customerId) FROM customer";
        try 
        {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(maxCustomerIDSQL);          
            if (rs.next()) 
            {
                maxCustomerID = rs.getInt(1);
            }
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
      return maxCustomerID + 1;
    }
    
    public static int addCustomer(Customer customer) 
    {
        int customerID = getMaxCustomerID();
        String addCustomerSQL = String.join(" ", 
                "INSERT INTO customer (customerId, customerName, addressId, active, "
                + "createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, ?, 1, NOW(), ?, NOW(), ?)");      
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(addCustomerSQL);
            stmt.setInt(1, customerID);
            stmt.setString(2, customer.getCustomerName());
            stmt.setInt(3, customer.getAddress().getAddressID());
            stmt.setString(4, loginUser.getUserName());
            stmt.setString(5, loginUser.getUserName());
            stmt.executeUpdate();
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
      return customerID;
    }
    
    public static void updateCustomer(Customer customer) 
    {
        String updateCustomerSQL = String.join(" ", 
                "UPDATE customer",
                "SET customerName=?, addressId=?, lastUpdate=NOW(), lastUpdateBy=?",
                "WHERE customerId = ?");     
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(updateCustomerSQL);
            stmt.setString(1, customer.getCustomerName());
            stmt.setInt(2, customer.getAddress().getAddressID());
            stmt.setString(3, loginUser.getUserName());
            stmt.setInt(4, customer.getCustomerID());
            stmt.executeUpdate();
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
    }
    
    public static void deleteCustomer(Customer customer) 
    {
        String deleteCustomerSQL = "UPDATE customer SET active=0 WHERE customerId = ?";       
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(deleteCustomerSQL);
            stmt.setInt(1, customer.getCustomerID());
            stmt.executeUpdate();
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
    }
       
}
