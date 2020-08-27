package view_controller;

import DAO.SQLappointmentDB;
import java.io.IOException;
import static java.lang.Math.abs;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import model.Appointment;

/**
 * FXML Controller class
 *
 * @author Kaleb Chatland
 */
public class WeekViewController implements Initializable
{
    private Stage stage;
    private Parent scene;
    private LocalDate selectedWeek;
    private static Appointment weekAppointment;  
    public static Appointment getModdedAppointment() {return weekAppointment;}
    private ObservableList<Appointment> calendarAppointments = FXCollections.observableArrayList();
    DateTimeFormatter weekFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
    DateTimeFormatter shortTime = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    
    @FXML
    private GridPane weekGridPane;
    
    @FXML
    private Label weekLabel;
    
    private List<Label> dateLabels;
    
    @FXML
    private Label SundayLabel;

    @FXML
    private Label MondayLabel;

    @FXML
    private Label TuesdayLabel;

    @FXML
    private Label WednesdayLabel;

    @FXML
    private Label ThursdayLabel;

    @FXML
    private Label FridayLabel;

    @FXML
    private Label SaturdayLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        dateLabels = new ArrayList<>();
        dateLabels.add(SundayLabel);
        dateLabels.add(MondayLabel);
        dateLabels.add(TuesdayLabel);
        dateLabels.add(WednesdayLabel);
        dateLabels.add(ThursdayLabel);
        dateLabels.add(FridayLabel);
        dateLabels.add(SaturdayLabel);

        LocalDate today = LocalDate.now();
        int offset = today.getDayOfWeek().getValue() % 7;
	selectedWeek = today.minusDays(offset);
        setWeekView();
    }    
    
    @FXML
    void goToMainMenuView(ActionEvent event) throws IOException 
    {
        String calendar = "mainMenu.fxml";
        goToNewScreen(event, calendar);
    }

    @FXML
    void goToManageAptmtViaCreateNewBTN(ActionEvent event) throws IOException 
    {
        weekAppointment = null;
        String calendar = "ManageAppointment.fxml";
        goToNewScreen(event, calendar);
    }

    @FXML
    void goToManageAptmtViaUpdateBTN(ActionEvent event) throws IOException 
    {
        if (weekAppointment == null)
        {
            JOptionPane.showMessageDialog(null, "No Appointment Selected");
        }else
            {   
                String manageAptmt = "ManageAppointment.fxml";
                goToNewScreen(event, manageAptmt);
            }
    }
     
    @FXML
    void goToCalendarView(ActionEvent event) throws IOException 
    {
        String calendar = "Calendar.fxml";
        goToNewScreen(event, calendar);
    }

    @FXML
    void loadNextWeek(ActionEvent event) 
    {
        selectedWeek = selectedWeek.plusWeeks(1);
        setWeekView();
    }

    @FXML
    void loadPreviousWeek(ActionEvent event) 
    {
        selectedWeek = selectedWeek.minusWeeks(1);
        setWeekView();
    }
    
    @FXML
    void deleteAppointmentData(ActionEvent event) 
    {
        if (weekAppointment == null)
        {
            JOptionPane.showMessageDialog(null, "No Appointment Selected");
        }
        else
            {// Message to user to confirm delete
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(weekAppointment.getType()+" will be deleted");
                alert.setContentText("Press OK to delete Appointment");
                Optional<ButtonType> rs = alert.showAndWait();        
                if (rs.get() == ButtonType.OK)
                {// User chose OK
                    SQLappointmentDB.deleteAppointment(weekAppointment);
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Message");
                    info.setHeaderText(null);
                    info.setContentText("Appointment successfully deleted");
                    info.showAndWait();
                } 
                else 
                    {// User chose CANCEL or closed the dialog
                        alert.close();                         
                    } 
        //Refresh calendar
        setWeekView();
        }
    }
    
    public void setWeekView() 
    {
        resetCalendar();

        weekLabel.setText("Week of " + selectedWeek.format(weekFormat));
        LocalDate lastDate = selectedWeek.plusWeeks(1);
        LocalDateTime startDateTime = LocalDateTime.of(selectedWeek, LocalTime.MIDNIGHT);
        LocalDateTime endDateTime = LocalDateTime.of(lastDate, LocalTime.MIDNIGHT);
        calendarAppointments = SQLappointmentDB.getAppointmentsInRange(startDateTime, endDateTime);
        applyDateLabels();
        applyHourLabels();       
        LocalDate date = selectedWeek;
        for (int columnIndex = 0; columnIndex < 7; columnIndex++) 
        {
            LocalDate currentDate = date;
            ObservableList<Appointment> selectAppointments = calendarAppointments.stream()
                .filter(a -> a.getStart().toLocalDate().equals(currentDate))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
            
            for (Appointment appointment : selectAppointments) 
            {
                LocalTime startTime = appointment.getStart().toLocalTime();          
                int hourBlock = startTime.getHour();              
                int minuteBlock = startTime.getMinute();
                int minuteOffset = 0;
                if (minuteBlock == 30) 
                {
                    minuteOffset = 1;
                }
                //absolute value placed to prevent negatives which throw exeptions
                //      (can't have a negative rowIndex in a gridpane)
                int rowIndex = abs(((hourBlock -9) * 2) + minuteOffset);            
                long duration = Duration.between(startTime, appointment.getEnd().toLocalTime()).toMinutes();
                int rowSpan = (int) (duration / 30);               
                TextArea appointmentBlock = getTimeBlock(appointment);
                weekGridPane.add(appointmentBlock, columnIndex + 1, rowIndex, 1, rowSpan);
            }
            date = date.plusDays(1);
        }
    }
    
    public void resetCalendar() 
    {
        weekAppointment = null;
        weekGridPane.getChildren().clear();
    }
    
    private void applyDateLabels() 
    {
        for (int i = 0; i < dateLabels.size(); i++) 
        {
            Label dateLabel = dateLabels.get(i);
            LocalDate setDate = selectedWeek.plusDays(i);
            dateLabel.setText(setDate.format(weekFormat));
        }
    }
    
    private void applyHourLabels() 
    {
        LocalTime time = LocalTime.MIDNIGHT.plusHours(9);
        for (int i = 0; i < 18; i++) 
        {
            Label timeLabel = new Label(time.format(shortTime));
            weekGridPane.add(timeLabel, 0, i);
            time = time.plusMinutes(30);
        }
    }
    
    private TextArea getTimeBlock(Appointment appointment) 
    {
        TextArea appointmentBlock = new TextArea(appointment.getType());
        appointmentBlock.setEditable(false);
        appointmentBlock.setWrapText(true);
        appointmentBlock.focusedProperty().addListener((obs, oldVal, newVal) -> weekAppointment = appointment);
        
        return appointmentBlock;
    }   
    
    private void goToNewScreen(ActionEvent event, String fileLocation) throws IOException
    {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(fileLocation));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
