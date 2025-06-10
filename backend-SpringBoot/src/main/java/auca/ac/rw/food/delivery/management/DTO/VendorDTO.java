package auca.ac.rw.food.delivery.management.DTO;

import java.util.List;

import auca.ac.rw.food.delivery.management.model.enums.VendorStatus;
import auca.ac.rw.food.delivery.management.model.enums.VendorType;

public class VendorDTO {
    private String name;
    private String location;
    private String phone;
    private String email;
    private VendorType vendorType;
    private String description;
    private String imageUrl;
    private VendorStatus status;

    // Constructors
    public VendorDTO() {}

    public VendorDTO(String name, String location, String phone, String email,
                           VendorType vendorType, String description, String imageUrl,
                           VendorStatus status) {
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.email = email;
        this.vendorType = vendorType;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public VendorType getVendorType() { return vendorType; }
    public void setVendorType(VendorType vendorType) { this.vendorType = vendorType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public VendorStatus getStatus() { return status; }
    public void setStatus(VendorStatus status) { this.status = status; }
}
