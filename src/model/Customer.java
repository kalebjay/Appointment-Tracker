package model;

/**
 *
 * @author Kaleb Chatland
 */
public class Customer
{
    private int customerID;
    private String customerName;
    private int addressID;
    private Address address;
    
    public Customer()
    {
        
    }
    
    public Customer(int customerID, String customerName)
    {
        this.customerID = customerID;
        this.customerName = customerName;
    }
    
    //setters or mutators
    public void setCustomerID(int customerID)
    {
        this.customerID = customerID;
    }
    
    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }
    
    public void setAddressID(int addressID)
    {
        this.addressID = addressID;
    }
    
    public void setAddress(Address address)
    {
        this.address = address;
    }
    
    //getters or accessors
    public int getCustomerID()
    {
        return customerID;
    }
    
    public String getCustomerName()
    {
        return customerName;
    }
    
    public int getAddressID()
    {
        return addressID;
    }
    
    public Address getAddress()
    {
        return address;
    }
    
    @Override
    public String toString() 
    {
        return getCustomerName();
    }
}
