package model;

/**
 *
 * @author Kaleb Chatland
 */
public class City
{
    private int cityID;
    private String city;
    private int countryID;
    private Country country;
    
    public City() 
    {
       
    }
    
    public City(int cityID) {
        this.cityID = cityID;
    }
    
    public City(int cityID, String city, int countryID) {
        this.cityID = cityID;
        this.city = city;
        this.countryID = countryID;
    }
    
    public City(int cityID, String city) {
        this.cityID = cityID;
        this.city = city;
    }
    
    public void setCityID(int cityID)
    {
        this.cityID = cityID;
    }
    
    public void setCityName(String city)
    {
        this.city = city;
    }
    
    public void setCountryID(int countryID)
    {
        this.countryID = countryID;
    }
    
    public void setCountry(Country country)
    {
        this.country = country;
    }
    
    public int getCityID()
    {
        return cityID;
    }
    
    public String getCityName()
    {
        return city;
    }
    
    public int getCountryID()
    {
        return countryID;
    }
    
    public Country getCountry()
    {
        return country;
    }
}
