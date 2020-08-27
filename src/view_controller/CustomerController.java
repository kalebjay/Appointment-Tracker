package view_controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.*;
import DAO.SQLcustomerDB;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javax.swing.JOptionPane;
/**
 * FXML Controller class
 *
 * @author Kaleb Chatland
 */

public class CustomerController implements Initializable
{
    private Stage stage;
    private Parent scene;
    private static Customer modifiedCustomer;
    public static Customer getModifiedCustomer() 
    {
        return modifiedCustomer;
    }
    @FXML
    private TableView<Customer> customerTableView;
    @FXML
    private TableColumn<Customer, String> Name;
    @FXML
    private TableColumn<Customer, String> Phone;
    @FXML
    private TableColumn<Customer, String> Address1;
    @FXML
    private TableColumn<Customer, String> Address2;
    @FXML
    private TableColumn<Customer, String> City;
    @FXML
    private TableColumn<Customer, String> Country;
    @FXML
    private TableColumn<Customer, String> PostalCode;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {   //Lambda expressions used to call SimpleStringProperties to populate the customer Table view       
        Name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        Phone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getPhone()));
        Address1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getAddress1()));
        Address2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getAddress2()));
        City.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getCity().getCityName()));      
        Country.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getCity().getCountry().getCountryName()));
        PostalCode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getPostalCode()));
            
        customerTableView.setItems(SQLcustomerDB.getAllCustomers());          
    }

    @FXML
    void goToMainMenuView(ActionEvent event) throws IOException 
    {
        String main = "mainMenu.fxml";
        goToNewScreen(event, main);
    }

    @FXML
    void goToManageCustomerNEW(ActionEvent event) throws IOException 
    {
        modifiedCustomer = null;
        String manCust = "ManageCustomer.fxml";
        goToNewScreen(event, manCust);
    }

    @FXML
    void goToManageCustomerMODIFY(ActionEvent event) throws IOException
    {
        modifiedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        String manCust = "ManageCustomer.fxml";
        goToNewScreen(event, manCust);
    }
    
    @FXML
    void deleteCustomer(ActionEvent event) throws IOException
    {   
        Customer customer = customerTableView.getSelectionModel().getSelectedItem();
        if (customer == null)
        {
            JOptionPane.showMessageDialog(null, "No Customer Selected");
        }else
        {// Message to user to confirm delete
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(customer.getCustomerName()+" will be deleted");
            alert.setContentText("Press OK to delete Customer");
            Optional<ButtonType> rs = alert.showAndWait();
        
            if (rs.get() == ButtonType.OK)
            {// User chose OK
                SQLcustomerDB.deleteCustomer(customer);
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Message");
                info.setHeaderText(null);
                info.setContentText("Customer successfully deleted");
                info.showAndWait();
            } else 
                {// User chose CANCEL or closed the dialog
                    alert.close();                         
                } 
        //Refresh tableview
        customerTableView.setItems(SQLcustomerDB.getAllCustomers());
        }
    }
    
    private void goToNewScreen(ActionEvent event, String fileLocation) throws IOException
    {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(fileLocation));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
          
    

