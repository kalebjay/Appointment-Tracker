package view_controller;

import DAO.SQLappointmentDB;
import DAO.SQLuserDB;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.application.Platform;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import model.User;

/**
 * FXML Controller class
 *
 * @author Kaleb Chatland
 */
public class LoginController implements Initializable
{
    ResourceBundle rb;
    Locale userLocale;
    private Stage stage;
    private Parent scene;
    public static User loginUser = new User();
    private final static Logger FILELOG = Logger.getLogger("userloginLog");
    
    @FXML
    private Label loginLabel;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField userNameTXT;

    @FXML
    private PasswordField passwordTXT;
    
    @FXML
    private Button loginBTN;
    
    @FXML
    private Button exitBTN;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        this.userLocale = Locale.getDefault();
        this.rb = ResourceBundle.getBundle("language_files/rb", this.userLocale);
        this.userNameLabel.setText(this.rb.getString("username"));
        this.passwordLabel.setText(this.rb.getString("password"));
        this.loginBTN.setText(this.rb.getString("loginBtnText"));
        this.exitBTN.setText(this.rb.getString("exitBtnText"));
        this.loginLabel.setText(this.rb.getString("loginLabel"));
    }
    
    @FXML
    void executeLogin(ActionEvent event) throws IOException
    {
        String loginUserName = userNameTXT.getText();
        String loginPassword = passwordTXT.getText();
        loginUser.setUserName(loginUserName);
        loginUser.setPassword(loginPassword);
        try
        {   
            User user = SQLuserDB.login();
            if(loginUser.getUserName().equals(user.getUserName()) && loginUser.getPassword().equals(user.getPassword()))
            {  
                try
                {  
                    Appointment upcomingAptmt = SQLappointmentDB.getUpcomingAptmt();
                    if (!(upcomingAptmt.getAppointmentID() == 0)) 
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Appointment Reminder");
                        alert.setHeaderText("Upcoming appointment in next 15 Min!");
                        alert.setContentText("You have an appointment scheduled" 
                        + "\non " + upcomingAptmt.getStart().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
                        + "\nat " + upcomingAptmt.getStart().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                        + " with " + upcomingAptmt.getCustomer().getCustomerName() + ".");
                        alert.showAndWait();                        
                    }
                    FILELOG.log(Level.INFO, "LOGIN: User ''{0}'' logged in.", loginUser.getUserName());
                    goToMainScreen(event);
                }
                catch(Exception e)
                    {
                        e.printStackTrace();
                    }
            }
            else
                {
                    FILELOG.log(Level.INFO, "UNSUCCESSFUL LOGIN.");
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(rb.getString("title"));
                    alert.setHeaderText(rb.getString("header"));
                    alert.setContentText(rb.getString("content"));
                    alert.showAndWait();                         
                }
        }
        catch(Exception e)
            {
                e.printStackTrace();
            }       
    }
    
    @FXML
    void exitProgram(ActionEvent event) 
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(rb.getString("exitTitle"));
        alert.setHeaderText(rb.getString("exitHeader"));
        alert.setContentText(rb.getString("exitContent"));
        Optional<ButtonType> rs = alert.showAndWait();
        //no place to put text for cancel button, otherwise I would add it in Russian
        if (rs.get() == ButtonType.OK)
        {   // user chose OK
            Platform.exit();
        } else 
            {   // user chose CANCEL or closed the dialog
                alert.close();
            } 
    }
    
    public static void createLogFile() 
    {
        FileHandler fh;  
        try 
        {  
            fh = new FileHandler("userloginLog.txt", true);  
            FILELOG.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);  
        } 
        catch (SecurityException |IOException e) 
            {  
                e.printStackTrace();   
            }  
    }
    
    private void goToMainScreen(ActionEvent event) throws IOException
    {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    
}
