package FinalProject;

/**
 * 
 * @author Group 15
 */
public class Customer {

    private final String username;
    private final String password;
    private int points;
    private String status;

    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
        this.points = 0;
        setStatus(points);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points += points;
        setStatus(this.points);
    }
    
    public String getStatus() {
        return this.status;
    }
    
    private void setStatus(int points) {
        if(points > 1000) {
            status = "GOLD";
        }
        else 
        {
            status = "SILVER";
        }
    }
}
