package auca.ac.rw.food.delivery.management.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
public class DeliveryDriver {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;
    
    private String vehiclePlate;
    private String status;

    @OneToMany(mappedBy = "deliveryDriver", cascade = CascadeType.ALL)
    private List<Order> orders;

    // Constructors
    public DeliveryDriver() {}

    public DeliveryDriver(String name, String phoneNumber, String vehiclePlate, String status) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.vehiclePlate = vehiclePlate;
        this.status = status;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public List<Order> getOrders() { return orders; }
    public String getVehiclePlate() { return vehiclePlate; }
    public String getStatus() { return status; }

    public void setName(String name) { this.name = name; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
}