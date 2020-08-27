package model;

import java.time.LocalDateTime;

/**
 *
 * @author Kaleb Chatland
 */
public class Appointment
{
    private int appointmentID;
    private int customerID;
    private int userID;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private Customer customer;
    
    public Appointment()
    {
        //empty constructor
    }
    
    public Appointment(int appointmentID, String type, LocalDateTime start, LocalDateTime end)
    {
        this.appointmentID = appointmentID;
        this.type = type;
        this.start = start;
        this.end = end;
    }
    
    public Appointment(int appointmentID)
    {
        this.appointmentID = appointmentID;
    }
    
    public void setAppointmentID(int appointmentID)
    {
        this.appointmentID = appointmentID;
    }
    
    public void setCustomerID(int customerID)
    {
        this.customerID = customerID;
    }
    
    public void setUserID(int userID)
    {
        this.userID = userID;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public void setStart(LocalDateTime start)
    {
        this.start = start;
    }
    
    public void setEnd(LocalDateTime end)
    {
        this.end = end;
    }
    
    public void setCustomer(Customer customer) 
    {
        this.customer = customer;
    }
    
    public int getAppointmentID()
    {
        return appointmentID;
    }
    
    public int getCustomerID()
    {
        return customerID;
    }
    
    public int getUserID()
    {
        return userID;
    }
    
    public String getType()
    {
        return type;
    }
    
    public LocalDateTime getStart()
    {
        return start;
    }
    
    public LocalDateTime getEnd()
    {
        return end;
    }
    
    public Customer getCustomer() 
    {
        return customer;
    }
    
    @Override
    public String toString() 
    {
        return getType();
    }
    
}
