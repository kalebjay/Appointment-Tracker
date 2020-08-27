package view_controller;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.*;
import DAO.*;
import javax.swing.JOptionPane;
import utils.Validator;
import static view_controller.CustomerController.getModifiedCustomer;

/**
 * FXML Controller class
 *
 * @author Kaleb Chatland
 */
public class ManageCustomerController implements Initializable
{
    private Stage stage;
    private Parent scene;
    private final Customer modifiedCustomer;
    public ManageCustomerController() {this.modifiedCustomer = getModifiedCustomer();}

    @FXML
    private TextField nameTXT;

    @FXML
    private TextField phoneTXT;

    @FXML
    private TextField address1TXT;

    @FXML
    private TextField address2TXT;

    @FXML
    private TextField cityTXT;

    @FXML
    private TextField countryTXT;

    @FXML
    private TextField postalCodeTXT;
 
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        if (modifiedCustomer != null) 
        {
            nameTXT.setText(modifiedCustomer.getCustomerName());
            address1TXT.setText(modifiedCustomer.getAddress().getAddress1());
            address2TXT.setText(modifiedCustomer.getAddress().getAddress2());
            phoneTXT.setText(modifiedCustomer.getAddress().getPhone());
            postalCodeTXT.setText(modifiedCustomer.getAddress().getPostalCode());
            cityTXT.setText(modifiedCustomer.getAddress().getCity().getCityName());
            countryTXT.setText(modifiedCustomer.getAddress().getCity().getCountry().getCountryName());
        }
    }
    
    @FXML
    void goToCustomerView(ActionEvent event) throws IOException 
    {
        String cust = "Customer.fxml";
        goToNewScreen(event , cust);
    }

    @FXML
    void saveCustomerData(ActionEvent event) throws IOException
    {
        String newName = nameTXT.getText();
        String newPhone = phoneTXT.getText();
        String newAddress1 = address1TXT.getText();
        String newAddress2 = address2TXT.getText();       
        String newCity = cityTXT.getText();
        String newCountry = countryTXT.getText();
        String newPostalCode = postalCodeTXT.getText();
        
        if (newName.equals("") || newPhone.equals("") || newAddress1.equals("")|| newCity.equals("")|| newCountry.equals("") || newPostalCode.equals(""))
        {
            JOptionPane.showMessageDialog(null, "Error, missing data");
        }
        if (modifiedCustomer == null) 
        {
            Country country = createNewCountry(newCountry);
            City city = createNewCity(newCity, country);
            Address address = createNewAddress(newPhone, newAddress1, newAddress2,  city, newPostalCode);
            try 
            {
                Validator.checkCustomerName(newName);
                Validator.checkAddress(address);
                createNewCustomer(newName, address);
            } catch (Exception e) 
                {
                    e.getMessage();
                }
        }
        else 
        {
            Address modifiedCustomerAddress = modifiedCustomer.getAddress();
            City modifiedCustomerCity = modifiedCustomerAddress.getCity();
            Country modifiedCustomerCountry = modifiedCustomerCity.getCountry();
            
            Country country = modifyCountry(modifiedCustomerCountry.getCountryID(), newCountry);
            City city = modifyCity(modifiedCustomerCity.getCityID(), newCity, country);
            Address address = modifyAddress(modifiedCustomerAddress.getAddressID(), newPhone, newAddress1, newAddress2, city, newPostalCode);
            
            try 
            {
                Validator.checkCustomerName(newName);
                Validator.checkAddress(address);
                modifyCustomer(modifiedCustomer.getCustomerID(), newName, address);
            } catch (Exception e) 
                {
                    e.getMessage();
                }
        }
        //After save is complete, return to Customer menu
        String cust = "Customer.fxml";
        goToNewScreen(event , cust);       
    }
       
    private Address createNewAddress(String phone, String address1, String address2, City city, String postalCode) 
    {
        Address address = new Address();
        address.setPhone(phone);
        address.setAddress1(address1);
        address.setAddress2(address2);
        address.setCity(city);
        address.setPostalCode(postalCode);
        int id = SQLaddressDB.addAddress(address);        
        address.setAddressID(id);
        
        return address;
    }
    
    private Address modifyAddress(int addressId, String phone, String address1, String address2, City city, String postalCode) 
    {
        Address address = new Address();   
        address.setAddressID(addressId);
        address.setPhone(phone);
        address.setAddress1(address1);
        address.setAddress2(address2);
        address.setCity(city);
        address.setPostalCode(postalCode);
        SQLaddressDB.updateAddress(address);

        return address;
    }
    
    private City createNewCity(String cityName, Country country) 
    {
        City city = new City();
        city.setCityName(cityName);
        city.setCountry(country);
        int id = SQLcityDB.addCity(city);
        city.setCityID(id);
        
        return city;
    }
    
    private City modifyCity(int cityID, String cityName, Country country) 
    {
        City city = new City();
        city.setCityID(cityID);
        city.setCityName(cityName);
        city.setCountry(country);
        SQLcityDB.updateCity(city);

        return city;
    }
    
    private Country createNewCountry(String countryName) 
    {
        Country country = new Country();
        country.setCountryName(countryName);    
        int id = SQLcountryDB.addCountry(country);
        country.setCountryID(id);
        
        return country;
    }
    
    private Country modifyCountry(int countryID, String countryName) 
    {
        Country country = new Country();
        country.setCountryID(countryID);
        country.setCountryName(countryName);     
        SQLcountryDB.updateCountry(country);
        
        return country;
    }
    
    private Customer createNewCustomer(String customerName, Address address) 
    {
        Customer customer = new Customer();
        customer.setCustomerName(customerName);
        customer.setAddress(address);
        int newId = SQLcustomerDB.addCustomer(customer);       
        customer.setCustomerID(newId);
        
        return customer;
    }
    
    private Customer modifyCustomer(int customerID, String customerName, Address address) 
    {
        Customer customer = new Customer();
        customer.setCustomerID(customerID);
        customer.setCustomerName(customerName);
        customer.setAddress(address);
        SQLcustomerDB.updateCustomer(customer);
        
        return customer;
    }

    private void goToNewScreen(ActionEvent event, String fileLocation) throws IOException
    {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(fileLocation));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
