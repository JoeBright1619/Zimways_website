package auca.ac.rw.food.delivery.management.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import auca.ac.rw.food.delivery.management.model.enums.*;

@Entity
public class DeliveryDriver {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String vehiclePlate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriverStatus status;

    @OneToMany(mappedBy = "deliveryDriver", cascade = CascadeType.ALL)
    private List<Order> orders;

    // Constructors
    public DeliveryDriver() {}

    public DeliveryDriver(String name, String phoneNumber, String vehiclePlate, DriverStatus status) {
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
    public DriverStatus getStatus() { return status; }

    public void setName(String name) { this.name = name; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
    public void setVehiclePlate(String vehiclePlate) { this.vehiclePlate = vehiclePlate; }
    public void setStatus(DriverStatus status) { this.status = status; }
}