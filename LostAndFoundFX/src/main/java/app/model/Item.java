package app.model;

public abstract class Item {

    private String title;
    private String category;
    private String location;
    private String description;
    private String status;
    private String imagePath;

    // 🔑 CONTACT INFO
    private String reporterName;
    private String phone;
    private String email;

    public Item(String title, String category, String location,
                String description, String imagePath,
                String reporterName, String phone, String email) {

        this.title = title;
        this.category = category;
        this.location = location;
        this.description = description;
        this.imagePath = imagePath;
        this.reporterName = reporterName;
        this.phone = phone;
        this.email = email;
        this.status = "OPEN";
    }

    public abstract String getType();

    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getImagePath() { return imagePath; }

    // 🔑 CONTACT GETTERS (ADMIN ONLY)
    public String getReporterName() { return reporterName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
}
