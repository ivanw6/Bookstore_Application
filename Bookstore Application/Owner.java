package FinalProject;

import java.io.IOException;
import java.util.ArrayList;
/**
 * 
 * @author Group 15
 */

public class Owner{
    protected static ArrayList<Book> books = new ArrayList<>(); 
    private static final ArrayList<Customer> customers = new ArrayList<>();
    private static final Files files = new Files();


    public String getUsername(){
        return "admin";
    }
    public String getPassword(){
        return "admin";
    }

    public void addCustomer(Customer created){
        customers.add(created);
    }

    public void deleteCustomer(Customer selected){
        customers.remove(selected);
    }
    
    public ArrayList<Customer> getCustomers(){
        return (ArrayList<Customer>) customers.clone();
    }
    
    public void restock() throws IOException {
        ArrayList<Book> tempBooks = files.readBookTxt();
        ArrayList<Customer> tempCustomers = files.readCustomerTxt();
        books.addAll(tempBooks);
        customers.addAll(tempCustomers);
    }
}