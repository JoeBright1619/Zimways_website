package auca.ac.rw.food.delivery.management.DTO;

public class LoginDTO {
    private String identifier; // email for customers, vendorId for vendors, adminId for admins
    private String password;

    // Getters and Setters
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
} 