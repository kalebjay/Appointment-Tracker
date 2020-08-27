package view_controller;

import DAO.SQLappointmentDB;
import DAO.SQLcustomerDB;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import model.Appointment;
import model.Customer;
import utils.Validator;
import static view_controller.CalendarController.getModifiedAppointment;
import static view_controller.WeekViewController.getModdedAppointment;

/**
 * FXML Controller class
 *
 * @author Kaleb Chatland
 */
public class ManageAppointmentController implements Initializable
{
    private Stage stage;
    private Parent scene;
    private LocalTime time;
    private final Appointment monthAppointment;
    private final Appointment weekAppointment;
    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    private ObservableList<String> dropdownTimes = FXCollections.observableArrayList();
    private ObservableList<String> types = FXCollections.observableArrayList();
    private DateTimeFormatter timeFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    
    @FXML
    private ComboBox<Customer> customerComboBox;

    @FXML
    private ComboBox<String> appointmentTypeComboBox;

    @FXML
    private ComboBox<String> startTimeComboBox;

    @FXML
    private ComboBox<String> endTimeComboBox;

    @FXML
    private DatePicker datePicker;
    
    //default constructor
    public ManageAppointmentController()
    {
        monthAppointment = getModifiedAppointment();
        weekAppointment = getModdedAppointment();  
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        time = LocalTime.MIDNIGHT.plusHours(9);//Start at 9 am, not before
        for (int i = 0; i < 17; i++) //17 is for 1700 hours or 5 pm
        {
            dropdownTimes.add(time.format(timeFormat));
            time = time.plusMinutes(30);
        }
        
        //get customers for combo box
        customers = SQLcustomerDB.getAllCustomers();
        customers.sort((a, b) -> a.getCustomerName().compareTo(b.getCustomerName()));
        
        // List of appointment types for combo box
        types = FXCollections.observableArrayList
        (
            "Presentation",
            "Scrum",    
            "All Hands Meeting",    
            "New Client",
            "Customer Support",
            "New Employee Orientation",
            "Staff Prof. Dev.",
            "Emergency"
        );

        // set the combo box with the item objects
        customerComboBox.setItems(customers);
        appointmentTypeComboBox.setItems(types);
        datePicker.setValue(LocalDate.now());      
        startTimeComboBox.setItems(dropdownTimes);
        endTimeComboBox.setItems(dropdownTimes);
        
        if (monthAppointment != null || weekAppointment != null) 
        {   
            if(monthAppointment != null)
            {// Data pulled from Calendar view variable
                customerComboBox.getSelectionModel().select(monthAppointment.getCustomer());
                appointmentTypeComboBox.getSelectionModel().select(monthAppointment.getType());
                datePicker.setValue(monthAppointment.getStart().toLocalDate());
                startTimeComboBox.getSelectionModel().select(monthAppointment.getStart().format(timeFormat));
                endTimeComboBox.getSelectionModel().select(monthAppointment.getEnd().format(timeFormat));              
            }
            else
            {//Data pulled from Week view variable
                customerComboBox.getSelectionModel().select(weekAppointment.getCustomer());
                appointmentTypeComboBox.getSelectionModel().select(weekAppointment.getType());
                datePicker.setValue(weekAppointment.getStart().toLocalDate());
                startTimeComboBox.getSelectionModel().select(weekAppointment.getStart().format(timeFormat));
                endTimeComboBox.getSelectionModel().select(weekAppointment.getEnd().format(timeFormat)); 
            }
        } 
    }
    
    @FXML
    void goToCalendarView(ActionEvent event) throws IOException 
    {
        String calendar = "Calendar.fxml";
        goToNewScreen(event , calendar);
    }

    @FXML
    void saveAppointmentData(ActionEvent event) throws IOException, Exception 
    {   // Values from GUI
        Customer aptmtCustomer = customerComboBox.getValue();
        String aptmtType = appointmentTypeComboBox.getValue();        
        LocalDate aptmtDate = datePicker.getValue();        
        String aptmtStart = startTimeComboBox.getValue();        
        String aptmetEnd = endTimeComboBox.getValue();    
        
        if (aptmtCustomer == null || aptmtType == null || aptmtDate == null || aptmtStart == null || aptmetEnd == null) 
        {
            JOptionPane.showMessageDialog(null, "Error, missing data");
        }// Combine date and times to make datetimes.
        LocalDateTime startDateTime = LocalDateTime.of(aptmtDate, LocalTime.parse(aptmtStart, timeFormat));
        LocalDateTime endDateTime = LocalDateTime.of(aptmtDate, LocalTime.parse(aptmetEnd, timeFormat));

        // Build the appointment object
        Appointment appointment = new Appointment();
        appointment.setCustomer(aptmtCustomer);
        appointment.setType(aptmtType);
        appointment.setStart(startDateTime); 
        appointment.setEnd(endDateTime);
        
        if(Validator.checkTimes(startDateTime, endDateTime) && Validator.checkAvailability(startDateTime, endDateTime))
        {
            if(!(monthAppointment == null && weekAppointment != null))
            {    
                if (monthAppointment == null) 
                {
                    try
                    {// Add a new appointment         
                        int newId = appointment.getAppointmentID();
                        SQLappointmentDB.addAppointment(appointment);
                        appointment.setAppointmentID(newId);
                    }
                    catch (Exception e) 
                        {
                            e.printStackTrace();
                        }
                }
                else 
                    {// Update the appointment data                   
                    appointment.setAppointmentID(monthAppointment.getAppointmentID());                   
                    SQLappointmentDB.updateAppointment(appointment);
                    }
                String calendar = "Calendar.fxml";
                goToNewScreen(event , calendar);
            }
            else
            {
                if (weekAppointment != null) 
                {
                    appointment.setAppointmentID(weekAppointment.getAppointmentID());                                      
                    SQLappointmentDB.updateAppointment(appointment);
                }
                String calendar = "Calendar.fxml";
                goToNewScreen(event , calendar); 
            }
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
