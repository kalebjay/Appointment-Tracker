package utils;

import DAO.SQLappointmentDB;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import model.*;

/**
 *
 * @author Kaleb Chatland
 */
public class Validator
{ 
    public static boolean checkAvailability(LocalDateTime start, LocalDateTime end) throws Exception 
    {
        ObservableList<Appointment> appointments = SQLappointmentDB.getOverlappingAptmts(start, end);

        if (appointments.size() > 0) 
        {
            JOptionPane.showMessageDialog(null, "Time conflict! Appointment overlaps an existing appointment");
            throw new CustomException("Appointments cannot overlap");
        }
        return true;
    }
    
    public static boolean checkTimes(LocalDateTime start, LocalDateTime end) throws CustomException 
    {
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate startDate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();
        int dayOfWeek = startDate.getDayOfWeek().getValue();
        LocalTime startTime = start.toLocalTime();
        LocalTime endTime = end.toLocalTime();
        
        if (!startDate.isEqual(endDate)) 
        {
            JOptionPane.showMessageDialog(null, "Time conflict! Appointments can not span multiple days");
            throw new CustomException("Appointment spans mulitiple days");
        }
        if (startTime.equals(endTime)) 
        {
            JOptionPane.showMessageDialog(null, "Time conflict! Start and End are the same");
            throw new CustomException("start and end are equal");
        } 
        if (dayOfWeek == 6 || dayOfWeek == 7) 
        {
            JOptionPane.showMessageDialog(null, "Time conflict! Weekend appoitments are not supported");
            throw new CustomException("Appointment on a weekend");
        }
        if (startTime.isBefore(midnight.plusHours(9))) 
        {
            JOptionPane.showMessageDialog(null, "Time conflict! Appointment is outside of busniess hours");
            throw new CustomException("Appointment outside business hours");
        }
        if (endTime.isAfter(midnight.plusHours(17))) 
        {
            JOptionPane.showMessageDialog(null, "Time conflict! Appointment is outside of busniess hours");
            throw new CustomException("Appointment outside business hours");
        } 
        if (endTime.isBefore(startTime)) 
        {
            JOptionPane.showMessageDialog(null, "Time conflict! Start time must be before end time");
            throw new CustomException("Appointment end is before start");
        }
        if (startTime.isAfter(endTime)) 
        {
            JOptionPane.showMessageDialog(null, "Time conflict! Start time must be before end time");
            throw new CustomException("Appointment end is before start");
        }
        
        return true;
    }
    
    public static boolean checkCustomerName(String customer) throws CustomException 
    {
        Customer tempCust = new Customer();
        customer = tempCust.getCustomerName();
        if (customer.equals("")) 
        {
            JOptionPane.showMessageDialog(null, "Name is blank");
            throw new CustomException("Customer name is required");
        }
        return true;
    }
    
    public static boolean checkAddress(Address address) throws CustomException 
    { 
        if (address.getPhone().equals("")) 
        {
            JOptionPane.showMessageDialog(null, "Phone number is required");
            throw new CustomException("Phone number is required");
        }
        if (address.getAddress1().equals("")) 
        {
            JOptionPane.showMessageDialog(null, "Address1 is required");
            throw new CustomException("Address1 is required");
        }
        if (address.getCity().getCityName().equals("")) 
        {
            JOptionPane.showMessageDialog(null, "City is required");
            throw new CustomException("City is required");
        }
        if (address.getCity().getCountry().getCountryName().equals("")) 
        {
            JOptionPane.showMessageDialog(null, "Country is required");
            throw new CustomException("Country is required");
        }
        if (address.getPostalCode().equals("")) 
        {
            JOptionPane.showMessageDialog(null, "Postal code is required");
            throw new CustomException("Postal code is required");
        }
        
        return true;
    }
    
}
