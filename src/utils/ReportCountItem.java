package utils;

/**
 *
 * @author Kaleb Chatland
 */
public class ReportCountItem
{
    private int count;
    private final String title;
    
    public ReportCountItem(String title)
    {
        this.title = title;
        this.count = 1;
    }
    
    public int getCount() 
    {
        return this.count;
    }
   
    public String getTitle() 
    {
        return this.title;
    }
    
    public void increment() 
    {
        this.count++;
    }
}
    

