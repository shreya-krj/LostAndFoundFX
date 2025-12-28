package app.model;

public class Admin extends User {

    public Admin(String username, String password) {
        super(username, password);
    }

    public void markItemReturned(Item item) {
        item.setStatus("RETURNED");
    }
}
