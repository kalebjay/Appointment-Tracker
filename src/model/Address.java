package model;

/**
 *
 * @author Kaleb Chatland
 */
public class Address
{
    private int addressID;
    private String phone;
    private String address1;
    private String address2;
    private int cityID;
    private String postalCode;
    private City city;
    
    public Address()
    {
       
    }
    
    public Address(int addressID, String address1, String address2, City city, String postalCode)
    {
        this.addressID = addressID;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.postalCode = postalCode;
    }
    
    public void setAddressID(int addressID)
    {
        this.addressID = addressID;
    }
    
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }
    
    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }
    
    public void setCityID(int cityID)
    {
        this.cityID = cityID;
    }
    
    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }
    
    public void setCity(City city)
    {
        this.city = city;
    }
    
    public int getAddressID()
    {
        return addressID;
    }
    
    public String getPhone()
    {
        return phone;
    }
    
    public String getAddress1()
    {
        return address1;
    }
    
    public String getAddress2()
    {
        return address2;
    }
    
    public int getCityID()
    {
        return cityID;
    }
    
    public String getPostalCode()
    {
        return postalCode;
    }
    
    public City getCity()
    {
        return city;
    }
    
    @Override
    public String toString() 
    {
        String fullAddress = String.join(" ",
            getAddress1(),
            getAddress2(),
            getCity().getCityName(),
            getPostalCode()
        );
      return fullAddress;
    }
}
