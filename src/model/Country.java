package model;

/**
 *
 * @author Kaleb Chatland
 */
public class Country
{
    private int countryID;
    private String country;
    
    public Country()
    {
        
    }
    
    public Country(int countryID, String country)
    {
        this.countryID = countryID;
        this.country = country;
    }
    
    public void setCountryID(int countryID)
    {
        this.countryID = countryID;
    }
    
    public void setCountryName(String country)
    {
        this.country = country;
    }
    
    public int getCountryID()
    {
        return countryID;
    }
    
    public String getCountryName()
    {
        return country;
    }
    
    @Override
    public String toString() 
    {
        return getCountryName();
    }
}
