package app.model;

public class User {

    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Encapsulation
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // password usually wouldn’t have a getter, but ok for mini project
    public String getPassword() {
        return password;
    }

    public boolean login(String enteredPassword) {
        return password.equals(enteredPassword);
    }
}
