package DAO;

import static DAO.DBconnection.conn;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;
import model.User;
import static view_controller.LoginController.loginUser;

/**
 *
 * @author Kaleb Chatland
 */
public class SQLappointmentDB
{
    private static final ZoneId zone = ZoneId.systemDefault();
    
    public static ObservableList<Appointment> getAptmtsByMonth() 
    {
        ObservableList<Appointment> aptmtsByMonth = FXCollections.observableArrayList();
        String getAptmtsByMonthSQL = "SELECT customer.*, appointment.* FROM customer "
                + "RIGHT JOIN appointment ON customer.customerId = appointment.customerId "
                + "WHERE start BETWEEN NOW() AND (SELECT LAST_DAY(NOW()))";
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(getAptmtsByMonthSQL);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                Customer selectedCustomer = SQLcustomerDB.getCustomerByID(rs.getInt("customerId"));
                Appointment getMonthlyAptmts = new Appointment();
                getMonthlyAptmts.setCustomer(selectedCustomer);
                getMonthlyAptmts.setAppointmentID(rs.getInt("appointmentId"));
                getMonthlyAptmts.setCustomerID(rs.getInt("customerId"));
                getMonthlyAptmts.setUserID(rs.getInt("userId"));
                getMonthlyAptmts.setType(rs.getString("type"));
                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = rs.getTimestamp("end").toLocalDateTime();
                LocalDateTime startLocal = startUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(zone).toLocalDateTime();
                LocalDateTime endLocal = endUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(zone).toLocalDateTime();           
                getMonthlyAptmts.setStart(startLocal);
                getMonthlyAptmts.setEnd(endLocal);
                getMonthlyAptmts.setCustomer(SQLcustomerDB.getCustomerByID(rs.getInt("customerId")));
                
                aptmtsByMonth.add(getMonthlyAptmts);
            }
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
      return aptmtsByMonth;
    }
    
    public static ObservableList<Appointment> getAptmtsByWeek() 
    {
        ObservableList<Appointment> aptmtsByWeek = FXCollections.observableArrayList();
        String getAptmtsByWeekSQL = "SELECT customer.*, appointment.* FROM customer "
                + "RIGHT JOIN appointment ON customer.customerId = appointment.customerId "
                + "WHERE start BETWEEN NOW() AND (SELECT ADDDATE(NOW(), INTERVAL 7 DAY))";
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(getAptmtsByWeekSQL);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                Customer selectedCustomer = SQLcustomerDB.getCustomerByID(rs.getInt("customerId"));
                Appointment getWeeklyAptmts = new Appointment();
                getWeeklyAptmts.setCustomer(selectedCustomer);
                getWeeklyAptmts.setAppointmentID(rs.getInt("appointmentId"));
                getWeeklyAptmts.setCustomerID(rs.getInt("customerId"));
                getWeeklyAptmts.setUserID(rs.getInt("userId"));
                getWeeklyAptmts.setType(rs.getString("type"));
                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = rs.getTimestamp("end").toLocalDateTime();
                LocalDateTime startLocal = startUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(zone).toLocalDateTime();
                LocalDateTime endLocal = endUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(zone).toLocalDateTime();
                getWeeklyAptmts.setStart(startLocal);
                getWeeklyAptmts.setEnd(endLocal);
                getWeeklyAptmts.setCustomer(SQLcustomerDB.getCustomerByID(rs.getInt("customerId")));
                
                aptmtsByWeek.add(getWeeklyAptmts);
            }
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
      return aptmtsByWeek;
    }
    
    public static ObservableList<Appointment> getAptmtsByUser(User user) 
    {
        ObservableList<Appointment> aptmtsByUser = FXCollections.observableArrayList();
        String getAptmtsByUserSQL = String.join(" ",
            "SELECT * FROM appointment AS a",
            "JOIN customer AS c",
            "ON a.customerId = c.customerId",
            "WHERE c.active = 1",
            "AND a.createdBy = ?"
           ); 
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(getAptmtsByUserSQL);
            stmt.setString(1, user.getUserName());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                Appointment appointment = new Appointment();
                appointment.setAppointmentID(rs.getInt("appointmentId"));
                appointment.setType(rs.getString("type"));             
                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = rs.getTimestamp("end").toLocalDateTime();
                LocalDateTime startLocal = startUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(zone).toLocalDateTime();
                LocalDateTime endLocal = endUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(zone).toLocalDateTime();               
                appointment.setStart(startLocal);
                appointment.setEnd(endLocal);
                appointment.setCustomer(SQLcustomerDB.getCustomerByID(rs.getInt("customerId")));
                
                aptmtsByUser.add(appointment);
            }
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
      return aptmtsByUser;
    }
    
    public static Appointment getAptmtByID(int appointmentID) 
    {
        Appointment getAptmtByID = new Appointment();
        String getAptmtByIDSQL = "SELECT customer.customerId, customer.customerName, appointment.* "
                + "FROM customer "
                + "RIGHT JOIN appointment ON customer.customerId = appointment.customerId " 
                + "WHERE appointmentId = ?";
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(getAptmtByIDSQL);
            stmt.setInt(1, appointmentID);
            ResultSet rs = stmt.executeQuery(); 
            if (rs.next()) 
            {
                Customer selectedCustomer = new Customer();
                selectedCustomer.setCustomerID(rs.getInt("customerId"));
                selectedCustomer.setCustomerName(rs.getString("customerName"));
                getAptmtByID.setCustomer(selectedCustomer);
                getAptmtByID.setCustomerID(rs.getInt("customerId"));
                getAptmtByID.setUserID(rs.getInt("userId"));
                getAptmtByID.setType(rs.getString("type"));               
                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = rs.getTimestamp("end").toLocalDateTime();
                LocalDateTime startLocal = startUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(zone).toLocalDateTime();
                LocalDateTime endLocal = endUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(zone).toLocalDateTime();              
                getAptmtByID.setStart(startLocal);
                getAptmtByID.setEnd(endLocal);
            }
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
      return getAptmtByID;
    }
    
    public static Appointment getUpcomingAptmt() 
    {
        Appointment upcomingAptmt = new Appointment();
        String getUpcomingAptmtSQL = "SELECT customer.customerName, appointment.* "
                + "FROM appointment "
                + "JOIN customer ON appointment.customerId = customer.customerId "
                + "WHERE (start BETWEEN ? AND ADDTIME(NOW(), '00:15:00'))";      
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(getUpcomingAptmtSQL);
            ZonedDateTime localZone = ZonedDateTime.now(zone);
            ZonedDateTime zdtUTC = localZone.withZoneSameInstant(ZoneId.of("UTC"));
            LocalDateTime localTime = zdtUTC.toLocalDateTime();
            stmt.setTimestamp(1, Timestamp.valueOf(localTime)); 
            ResultSet rs = stmt.executeQuery();          
            while (rs.next()) 
            {
                Customer customer = new Customer();
                customer.setCustomerName(rs.getString("customerName"));
                upcomingAptmt.setCustomer(customer);
                upcomingAptmt.setAppointmentID(rs.getInt("appointmentId"));
                upcomingAptmt.setCustomerID(rs.getInt("customerId"));
                upcomingAptmt.setUserID(rs.getInt("userId"));
                upcomingAptmt.setType(rs.getString("type"));
                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime startLocal = startUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(zone).toLocalDateTime();
                upcomingAptmt.setStart(startLocal);
            }
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
      return upcomingAptmt;
    }
    
    public static ObservableList<Appointment> getAppointmentsInRange(LocalDateTime start, LocalDateTime end) 
    {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();       
        String getAppointmentQuery = String.join(" ",
            "SELECT * FROM appointment AS a",
            "JOIN customer AS c",
            "ON a.customerId = c.customerId",
            "WHERE a.start >= ? AND a.end <= ?",
            "AND c.active = 1");
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(getAppointmentQuery);      
            LocalDateTime startDatetimeParam = start.atZone(zone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            LocalDateTime endDatetimeParam = end.atZone(zone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            stmt.setTimestamp(1, Timestamp.valueOf(startDatetimeParam));
            stmt.setTimestamp(2, Timestamp.valueOf(endDatetimeParam));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                Appointment targetAptmt = new Appointment();
                targetAptmt.setAppointmentID(rs.getInt("appointmentId"));
                targetAptmt.setType(rs.getString("type"));               
                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = rs.getTimestamp("end").toLocalDateTime();
                LocalDateTime startLocal = startUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(zone).toLocalDateTime();
                LocalDateTime endLocal = endUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(zone).toLocalDateTime();  
                targetAptmt.setStart(startLocal);
                targetAptmt.setEnd(endLocal); 
                targetAptmt.setCustomer(SQLcustomerDB.getCustomerByID(rs.getInt("customerId")));
                
                appointments.add(targetAptmt);
            }
        } 
        catch(SQLException e)
            {
                e.printStackTrace();
            }       
      return appointments;
    }
    
    public static ObservableList<Appointment> getOverlappingAptmts(LocalDateTime start, LocalDateTime end) 
    {
        ObservableList<Appointment> getOverlappedAptmts = FXCollections.observableArrayList();
        String getOverlappingAptmtsSQL = String.join(" ",
            "SELECT * FROM appointment AS a",
            "JOIN customer AS c",
            "ON a.customerId = c.customerId",
            "WHERE (a.start >= ? AND a.end <= ?)",
            "OR (a.start <= ? AND a.end >= ?)",
            "OR (a.start BETWEEN ? AND ? OR a.end BETWEEN ? AND ?)",
            "AND c.active = 1"
           );  
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(getOverlappingAptmtsSQL);           
            LocalDateTime startDT = start.atZone(zone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            LocalDateTime endDT = end.atZone(zone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();          
            stmt.setTimestamp(1, Timestamp.valueOf(startDT));
            stmt.setTimestamp(2, Timestamp.valueOf(endDT));
            stmt.setTimestamp(3, Timestamp.valueOf(startDT));
            stmt.setTimestamp(4, Timestamp.valueOf(endDT));
            stmt.setTimestamp(5, Timestamp.valueOf(startDT));
            stmt.setTimestamp(6, Timestamp.valueOf(endDT));
            stmt.setTimestamp(7, Timestamp.valueOf(startDT));
            stmt.setTimestamp(8, Timestamp.valueOf(endDT));
            ResultSet rs = stmt.executeQuery();         
            while (rs.next()) 
            {
                Appointment overlappedAptmt = new Appointment();
                overlappedAptmt.setAppointmentID(rs.getInt("appointmentId"));
                overlappedAptmt.setType(rs.getString("type"));              
                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = rs.getTimestamp("end").toLocalDateTime();
                LocalDateTime startLocal = startUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(zone).toLocalDateTime();
                LocalDateTime endLocal = endUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(zone).toLocalDateTime();
                overlappedAptmt.setStart(startLocal);
                overlappedAptmt.setEnd(endLocal);
                overlappedAptmt.setCustomer(SQLcustomerDB.getCustomerByID(rs.getInt("customerId")));
                
                getOverlappedAptmts.add(overlappedAptmt);
            }
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
      return getOverlappedAptmts;
    } 
    
    private static int getMaxAppointmentID() 
    {
        int maxAppointmentID = 0;
        String maxAppointmentIDSQL = "SELECT MAX(appointmentId) FROM appointment";       
        try 
        {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(maxAppointmentIDSQL);
            if (rs.next()) 
            {
                maxAppointmentID = rs.getInt(1);
            }
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
        return maxAppointmentID + 1;
    }

    public static int addAppointment(Appointment appointment) 
    {
        int appointmentID = getMaxAppointmentID();
        String addAppointmentSQL = String.join(" ",
                "INSERT INTO appointment (appointmentId, customerId, userId, title, "
                + "description, location, contact, type, url, start, end, "
                + "createDate, createdBy, lastUpdate, lastUpdateBy) ",
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?)");      
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(addAppointmentSQL);
            stmt.setInt(1, appointmentID);
            stmt.setObject(2, appointment.getCustomer().getCustomerID());
            stmt.setObject(3, "1");//userId
            stmt.setObject(4, "not needed");//title
            stmt.setObject(5, "not needed");//description
            stmt.setObject(6, "not needed");//location
            stmt.setObject(7, "not needed");//contact
            stmt.setObject(8, appointment.getType());
            stmt.setObject(9, "not needed");//url                
            LocalDateTime startUTC = appointment.getStart().atZone(zone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            LocalDateTime endUTC = appointment.getEnd().atZone(zone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            stmt.setTimestamp(10, Timestamp.valueOf(startUTC));
            stmt.setTimestamp(11, Timestamp.valueOf(endUTC));
            stmt.setString(12, loginUser.getUserName());
            stmt.setString(13, loginUser.getUserName());
            stmt.executeUpdate();
        }
        catch (SQLException e)
            {
                e.printStackTrace();
            }
        return appointmentID;
    }
    
    public static void updateAppointment(Appointment appointment) 
    {
        String updateAptmtSQL = String.join(" ",
                "UPDATE appointment",
                "SET customerId=?, userId=?, title=?, description=?, location=?," +
                "contact=?, type=?, url=?, start=?, end=?, lastUpdate=NOW(), lastUpdateBy=?",
                "WHERE appointmentId=?");
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(updateAptmtSQL);
            stmt.setObject(1, appointment.getCustomer().getCustomerID());
            stmt.setObject(2, "1");//userId
            stmt.setObject(3, "not needed");//title
            stmt.setObject(4, "not needed");//description
            stmt.setObject(5, "not needed");//location
            stmt.setObject(6, "not needed");//contact
            stmt.setObject(7, appointment.getType());
            stmt.setObject(8, "not needed");//url
            LocalDateTime startUTC = appointment.getStart().atZone(zone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            LocalDateTime endUTC = appointment.getEnd().atZone(zone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            stmt.setTimestamp(9, Timestamp.valueOf(startUTC));
            stmt.setTimestamp(10, Timestamp.valueOf(endUTC));
            stmt.setString(11, loginUser.getUserName());
            stmt.setObject(12, appointment.getAppointmentID());
            stmt.executeUpdate();
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
    }
    
    public static void deleteAppointment(Appointment appointment) 
    {
        String deleteAppointmentSQL = "DELETE FROM appointment WHERE appointmentId = ?";
        try 
        {
            PreparedStatement stmt = conn.prepareStatement(deleteAppointmentSQL);
            stmt.setObject(1, appointment.getAppointmentID());
            stmt.executeUpdate();
        }
        catch (SQLException e) 
            {
                e.printStackTrace();
            }
    }
}
