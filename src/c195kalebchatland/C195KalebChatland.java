package c195kalebchatland;

import java.sql.SQLException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import DAO.DBconnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import view_controller.LoginController;

/**
 *36 coding files in all
 * @author Kaleb Chatland
 */
public class C195KalebChatland extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/Login.fxml"));
        Scene scene = new Scene(root);       
        stage.setScene(scene);
        stage.show();
    }

    /**Main method
     * @param args the command line arguments
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException, Exception
    {   //Lambda expressions can be found at the following:
        //CalendarController line 181
        //CustomerController lines 58-64
        DBconnection.startConnection();
        LoginController.createLogFile();
        launch(args);
        DBconnection.closeConnection();
    }
    
}
