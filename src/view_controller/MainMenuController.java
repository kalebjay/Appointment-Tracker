package view_controller;

import java.io.IOException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kaleb Chatland
 */
public class MainMenuController 
{   
    private Stage stage;
    private Parent scene;
    
    @FXML
    void exitProgram(ActionEvent event) 
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Program");
        alert.setHeaderText("Exit the program?");
        alert.setContentText("Press OK to exit");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {   // user chose OK
            Platform.exit();
        } else 
            {   // user chose CANCEL or closed the dialog
                alert.close();
            } 
    }

    @FXML
    void goToCalendarView(ActionEvent event) throws IOException 
    {
        String calendar = "Calendar.fxml";
        goToNewScreen(event , calendar);
    }

    @FXML
    void goToCustomersView(ActionEvent event) throws IOException 
    {   
        String cust = "Customer.fxml";
        goToNewScreen(event , cust);
    }

    @FXML
    void goToReportsView(ActionEvent event) throws IOException 
    {
        String file = "Reports.fxml";
        goToNewScreen(event , file);
    }
        
    private void goToNewScreen(ActionEvent event, String fileLocation) throws IOException
    {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(fileLocation));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
