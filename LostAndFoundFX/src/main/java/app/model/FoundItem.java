package app.model;

public class FoundItem extends Item {

    public FoundItem(String title, String category, String location,
                     String description, String imagePath,
                     String reporterName, String phone, String email) {

        super(title, category, location, description,
                imagePath, reporterName, phone, email);
    }

    @Override
    public String getType() {
        return "FOUND";
    }
}

