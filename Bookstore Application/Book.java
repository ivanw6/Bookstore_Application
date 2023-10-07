package FinalProject;

import javafx.scene.control.CheckBox;

/**
 * 
 * @author Group 15
 */
public class Book {
    
    private String name;
    private double price;
    public CheckBox select;
    
    
    public Book(String name, double price) {
        this.name = name;
        this.price = price;
        this.select = new CheckBox();
    }
    
    public String getName(){
        return this.name;
    }
    
    public double getPrice(){
        return this.price;
    }
    
    public CheckBox getSelect() {
        return select;
    }
    
    public void setSelect(CheckBox select)
    {
        this.select = select;
    }
}
