package auca.ac.rw.food.delivery.management.model;

import auca.ac.rw.food.delivery.management.model.enums.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
    name = "restaurant",
    uniqueConstraints = @UniqueConstraint(columnNames = {"name", "address"})
)
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING) 
    private RestaurantStatus status;
    
    

    // Constructors
    public Restaurant() {}

    public Restaurant(String name, String location, String phone, RestaurantStatus status) {
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.status = status;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getLocation() { return location; }
    public RestaurantStatus getStatus() { return status; }
  

    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }
    public void setStatus(RestaurantStatus status) { this.status = status; }
    public void setPhone(String phone) { this.phone = phone; }

    // Helper method to link menu items
    

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", phone='" + phone + '\'' +
                ", status='" + status + '\'' +
                
                '}';
    }
}
