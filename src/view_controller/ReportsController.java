package view_controller;

import utils.ReportCountList;
import utils.ReportCountItem;
import DAO.SQLappointmentDB;
import DAO.SQLcustomerDB;
import DAO.SQLuserDB;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

/**
 * FXML Controller class
 *
 * @author Kaleb Chatland
 */
public class ReportsController implements Initializable
{
    private Stage stage;
    private Parent scene;
    private YearMonth selectedMonth;
    private ReportCountList counts;
    private static User selectedUser;
    private final ObservableList<User> users;
    private final ObservableList<Customer> customers;
    private final ObservableList<YearMonth> months = FXCollections.observableArrayList();
    DateTimeFormatter yearMonthFormat = DateTimeFormatter.ofPattern("MMM", Locale.getDefault());
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
    
    @FXML//*******************************************************
    private Tab typeTab;
    
    @FXML
    private TableView<ReportCountItem> aptmtTypeTableView;
    
    @FXML
    private TableColumn<ReportCountItem, String> typeCol;

    @FXML
    private TableColumn<ReportCountItem, Integer> quantCol;
    
    @FXML
    private ComboBox<YearMonth> monthComboBox;

    @FXML//********************************************************
    private Tab scheduleTab;

    @FXML
    private TableView<Appointment> scheduleTableView;
    
    @FXML
    private TableColumn<Appointment, String> customerSchedCol;
    
    @FXML
    private TableColumn<Appointment, String> typeSchedCol;
    
    @FXML
    private TableColumn<Appointment, String> dateSchedCol;
    
    @FXML
    private TableColumn<Appointment, String> startSchedCol;

    @FXML
    private TableColumn<Appointment, String> endSchedCol;
    
    @FXML
    private ComboBox<User> consultantComboBox;

    @FXML//*********************************************************
    private Tab countryTab;

    @FXML
    private TableView<Customer> countryTableView;
    
    @FXML
    private TableColumn<Customer, String> customerCol;
            
    @FXML
    private TableColumn<Customer, String> cityCol;
    
    @FXML
    private TableColumn<Customer, String> countryCol;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {// Initialize Type tab
        selectedMonth = YearMonth.now();
        monthComboBox.setItems(months);
        monthComboBox.getSelectionModel().select(selectedMonth);
        typeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        quantCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCount()).asObject());
        
        popAppointmentTypeTable();
        
        //Initialize Schedule Tab
        consultantComboBox.setItems(users);//consultant combo box
        
        customerSchedCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getCustomerName()));
        typeSchedCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        dateSchedCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStart().format(dateFormat)));
        startSchedCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStart().format(timeFormat)));
        endSchedCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd().format(timeFormat)));
        
        //Initialize Country Tab
        customerCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        cityCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getCity().getCityName()));      
        countryCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getCity().getCountry().getCountryName()));
        
        countryTableView.setItems(customers);
    }  
    
    public ReportsController() 
    {
        // populate a list of months
        YearMonth currYM = YearMonth.now();
        int currYear = currYM.getYear();
        for (int i = 1; i <= 12; i++) 
        {
            months.add(YearMonth.of(currYear, i));
        }
        //get users and cusomters for other two tabs
        users = SQLuserDB.getAllUsers();
        customers = SQLcustomerDB.getAllCustomers();
    }
    
     @FXML
    void launchTypeReport(ActionEvent event) 
    {
        YearMonth chosenMonth = monthComboBox.getValue();
        
        if (chosenMonth != null) 
        {
            selectedMonth = chosenMonth;
            popAppointmentTypeTable();
        }
        else 
        {// fix this alert 
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Select a Month");
            alert.setHeaderText("Select a Month");
            alert.setContentText("Please select a month to view.");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
        }
    }
    
    @FXML
    void launchScheduleReport(ActionEvent event) 
    {
        User chosenUser = consultantComboBox.getValue();
        
        if (chosenUser != null) 
        {
            selectedUser = chosenUser;
            popScheduleTable();
        }
        else 
        {// fix this alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Select a User");
            alert.setHeaderText("Select a User");
            alert.setContentText("Please select a user to view.");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
        }
    }
    
    @FXML
    void returnToMainMenu(ActionEvent event) throws IOException 
    {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    } 
    
    private void popAppointmentTypeTable() 
    {
        
        if (selectedMonth != null) 
        {
            LocalDate firstDate = selectedMonth.atDay(1);
            LocalDate lastDate = selectedMonth.atEndOfMonth();
            LocalDateTime startDatetime = LocalDateTime.of(firstDate, LocalTime.MIDNIGHT);
            LocalDateTime endDatetime = LocalDateTime.of(lastDate, LocalTime.MIDNIGHT);
            
            ObservableList<Appointment> appointments = SQLappointmentDB.getAppointmentsInRange(startDatetime, endDatetime);

            counts = new ReportCountList();
            
            for (Appointment appointment : appointments) 
            {
                String type = appointment.getType();
                int index = counts.indexOf(type);
                if (index == -1) 
                {
                    ReportCountItem item = new ReportCountItem(type);
                    counts.add(item);
                }
                else 
                {
                    ReportCountItem item = counts.get(index);
                    item.increment();
                }
            }
            aptmtTypeTableView.setItems(counts.list());
        }
    }
    
    private void popScheduleTable() 
    {   
        if (selectedUser != null) 
        {
            ObservableList<Appointment> appointments = SQLappointmentDB.getAptmtsByUser(selectedUser);
            appointments.sort((a, b) -> a.getStart().toLocalDate().compareTo(b.getStart().toLocalDate()));
            
            scheduleTableView.setItems(appointments);
        }
    }
    
}
