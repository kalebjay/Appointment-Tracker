package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Kaleb Chatland
 */
public class ReportCountList
{
    private ObservableList<ReportCountItem> items = FXCollections.observableArrayList();
 
    public ReportCountList() 
    {
        
    }
    
    public void add(ReportCountItem item) 
    {
        this.items.add(item);
    }
    
    public int indexOf(String item) 
    {
        int index = 0;
        for (ReportCountItem i : this.items) 
        {
            if (i.getTitle().equals(item)) 
            {
                return index;
            }
            
            index++;
        }

        return -1;
    }
    
    public ReportCountItem get(int index) 
    {
        return this.items.get(index);
    }
    
    public ObservableList<ReportCountItem> list() 
    {
        return this.items;
    }
    
    public int size() {
        return this.items.size();
    } 
    
    
}
