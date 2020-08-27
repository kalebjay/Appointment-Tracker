package view_controller;

import DAO.SQLappointmentDB;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import model.*;

/**
 * FXML Controller class
 *
 * @author Kaleb Chatland
 */
public class CalendarController implements Initializable
{   
    private Stage stage;
    private Parent scene;
    private YearMonth selectedMonth;  
    private ObservableList<Appointment> calendarAppointments = FXCollections.observableArrayList();
    private static Appointment monthAppointment;
    public static Appointment getModifiedAppointment() {return monthAppointment;}
    DateTimeFormatter yearMonthFormat = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault());
    
    @FXML
    private GridPane monthGridpane;
    
    @FXML
    public Label monthViewLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        selectedMonth = YearMonth.now();
        setCalendarView();
    }
    
    @FXML
    void goToMainMenuView(ActionEvent event) throws IOException 
    {
        String main = "mainMenu.fxml";
        goToNewScreen(event, main);
    }
    
    @FXML
    void goToManageAptmtViaCreateNewBTN(ActionEvent event) throws IOException 
    {
        monthAppointment = null;
        String manageAptmt = "ManageAppointment.fxml";
        goToNewScreen(event, manageAptmt);
    }
    
    @FXML
    void goToManageAptmtViaUpdateBTN(ActionEvent event) throws IOException 
    {
        if (monthAppointment == null)
        {
            JOptionPane.showMessageDialog(null, "No Appointment Selected");
        }else
            {    
                String manageAptmt = "ManageAppointment.fxml";
                goToNewScreen(event, manageAptmt);
            }
    }
    
    @FXML
    void loadNextMonth(ActionEvent event) 
    {
        selectedMonth = selectedMonth.plusMonths(1);
        setCalendarView();
    }
    
    @FXML
    void loadPreviousMonth(ActionEvent event) 
    {
        selectedMonth = selectedMonth.minusMonths(1);
        setCalendarView();
    }
    
     @FXML
    void goToWeeklyView(ActionEvent event) throws IOException 
    { 
        String manageAptmt = "WeekView.fxml";
        goToNewScreen(event, manageAptmt);
    }

    @FXML
    void deleteAppointmentData(ActionEvent event)  
    {   
        if (monthAppointment == null)
        {
            JOptionPane.showMessageDialog(null, "No Appointment Selected");
        }
        else
            {// Message to user to confirm delete
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(monthAppointment.getType()+" will be deleted");
                alert.setContentText("Press OK to delete Appointment");
                Optional<ButtonType> rs = alert.showAndWait();
                if (rs.get() == ButtonType.OK)
                    {// User chose OK
                        SQLappointmentDB.deleteAppointment(monthAppointment);
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
        setCalendarView();
        }
    }
    
    public void setCalendarView() 
    {
        resetCalendar();

        monthViewLabel.setText(selectedMonth.format(yearMonthFormat));
        LocalDate firstDate = selectedMonth.atDay(1);
        LocalDate lastDate = selectedMonth.atEndOfMonth();
        LocalDateTime startDatetime = LocalDateTime.of(firstDate, LocalTime.MIDNIGHT);
        LocalDateTime endDatetime = LocalDateTime.of(lastDate, LocalTime.MIDNIGHT);
        calendarAppointments = SQLappointmentDB.getAppointmentsInRange(startDatetime, endDatetime);
        int monthlyOffset = getMonthOffset();
        for (LocalDate date = firstDate; date.isBefore(lastDate.plusDays(1)); date = date.plusDays(1)) 
        {
            int dayOfMonth = date.getDayOfMonth();
            int index = dayOfMonth - monthlyOffset;
            BorderPane datePane = getDatePane(dayOfMonth, date);
            monthGridpane.add(datePane, index % 7, index / 7);
        }
    }
    
    public void resetCalendar() 
    {
        monthAppointment = null;
        monthGridpane.getChildren().clear();
    }
    
    private BorderPane getDatePane(int dayofMonth, LocalDate currentDate) 
    {
        BorderPane datePane = new BorderPane();
        Label dateLabel = new Label();
        dateLabel.setText(Integer.toString(dayofMonth));
        datePane.setTop(dateLabel);
        BorderPane.setAlignment(dateLabel, Pos.TOP_RIGHT);
        
        //Lambda expression to filter the start date of appointments to match with the current date, 
        //helps stream the appointments to populate the list view 
        ObservableList<Appointment> selectAppointments = calendarAppointments.stream()
            .filter(a -> a.getStart().toLocalDate().equals(currentDate))
            .collect(Collectors.toCollection(FXCollections::observableArrayList));
        ListView<Appointment> listView = new ListView<>(selectAppointments);         
        listView.getSelectionModel().selectedItemProperty()
            .addListener((obs, oldVal, newVal) -> monthAppointment = newVal);        
        datePane.setCenter(listView);
        
        return datePane;
    }
    
    private int getMonthOffset() {
        LocalDate firstDay = selectedMonth.atDay(1);
        DayOfWeek day = firstDay.getDayOfWeek();
        int offset = (day.getValue() - 1) % 7;
        LocalDate firstSunday = firstDay.minusDays(offset);
        Period span = Period.between(firstDay, firstSunday);

        return span.getDays();
    }
    
    private void goToNewScreen(ActionEvent event, String fileLocation) throws IOException
    {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(fileLocation));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}

