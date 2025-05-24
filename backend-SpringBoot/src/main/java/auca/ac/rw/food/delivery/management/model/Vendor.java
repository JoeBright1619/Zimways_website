package auca.ac.rw.food.delivery.management.model;

import auca.ac.rw.food.delivery.management.model.enums.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(
    name = "vendor",
    uniqueConstraints = @UniqueConstraint(columnNames = {"name", "location"})
)

public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
   
    private String name;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String email;
    

    private String description; 
    private String imageUrl;
    private double averageRating = 0.0;
    private int totalRatings = 0;

    @Enumerated(EnumType.STRING)
  
    private VendorType vendorType;

    @Enumerated(EnumType.STRING) 
    private VendorStatus status = VendorStatus.OPEN; // or ACTIVE, whatever fits your logic

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();
    

    // Constructors
    public Vendor() {}

    public Vendor(String name, String location, String phone, String email, VendorType vendorType,
              VendorStatus status, String description, String imageUrl) {
    this.name = name;
    this.location = location;
    this.phone = phone;
    this.email = email;
    this.vendorType = vendorType;
    this.status = status;
    this.description = description;
    this.imageUrl = imageUrl;
}

    // Getters and Setters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getLocation() { return location; }
    public VendorStatus getStatus() { return status; }
    public List<Item> getItems() { return items; }
    public String getEmail() { return email; }
    public VendorType getVendorType() { return vendorType; }
    public double getAverageRating() { return averageRating; }
    public int getTotalRatings() { return totalRatings; }

    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }
    public void setStatus(VendorStatus status) { this.status = status; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setItems(List<Item> items) { this.items = items; }
    public void setEmail(String email) { this.email = email; }
    public void setVendorType(VendorType vendorType) { this.vendorType = vendorType; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public void setTotalRatings(int totalRatings) { this.totalRatings = totalRatings; }

    // Helper method to link menu items
    

    @Override
    public String toString() {
        return "Vendor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", phone='" + phone + '\'' +
                ", status='" + status + '\'' +
                
                '}';
    }
}
