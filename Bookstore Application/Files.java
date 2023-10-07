package FinalProject;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * 
 * @author Group 15
 */
public class Files {

    public void bookTxtWrite(ArrayList<Book> books) throws IOException{
        FileWriter bookTxt = new FileWriter("books.txt", true);
        for(Book b: books){
            String bookInfo = b.getName() + ", " + b.getPrice() + "\n";
            bookTxt.write(bookInfo);
        }
        bookTxt.close();
    }//this copies all current books in books arraylist into file

    public void customerTxtWrite(ArrayList<Customer> customers) throws IOException{
        FileWriter customerTxt = new FileWriter("customers.txt", true);
        for(Customer c: customers){
            String outputText = c.getUsername() + ", " + c.getPassword() + ", " + c.getPoints() + "\n";
            customerTxt.write(outputText);
        }
        customerTxt.close();
    }

    public void bookTxtReset() throws IOException {
        FileWriter bookTxt = new FileWriter("books.txt", false);
        bookTxt.close();
    }//this resets the entire file so we can copy the new book stock in the arraylist

    public void customerTxtReset() throws IOException {
        FileWriter customerTxt = new FileWriter("customers.txt", false);
        customerTxt.close();
    }

    public ArrayList<Book> readBookTxt() throws IOException{
        Scanner scanner = new Scanner(new FileReader("books.txt"));
        ArrayList<Book> tempBookHolder = new ArrayList<>();

        while(scanner.hasNext()) {
            String[] bookInfo = scanner.nextLine().split(",");
            String title = bookInfo[0];
            double price = Double.parseDouble(bookInfo[1]);
            tempBookHolder.add(new Book(title, price));
        }
        return tempBookHolder;
    }//reads in from file puts everything in a temp object array and sends array to be copied from to actual array

    public ArrayList<Customer> readCustomerTxt() throws IOException{
        Scanner scanner = new Scanner(new FileReader("customers.txt"));
        ArrayList<Customer> tempCustomerHolder = new ArrayList<>();

        while(scanner.hasNext()) {
            String[] customerInfo = scanner.nextLine().split(", ");
            String username = customerInfo[0];
            String password = customerInfo[1];
            int points = Integer.parseInt(customerInfo[2]);
            tempCustomerHolder.add(new Customer(username, password));
            tempCustomerHolder.get(tempCustomerHolder.size()-1).setPoints(points);
        }
        return tempCustomerHolder;
    }

}