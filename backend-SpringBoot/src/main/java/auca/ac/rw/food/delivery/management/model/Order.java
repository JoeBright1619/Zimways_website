package auca.ac.rw.food.delivery.management.model;

import auca.ac.rw.food.delivery.management.model.enums.*;


import jakarta.persistence.*;


import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;  // Order is placed based on the Cart

    @ManyToOne
    @JoinColumn(name = "driver_id")  // Link to DeliveryDriver
    private DeliveryDriver deliveryDriver;


    private Double total;
    private LocalDateTime orderDate;
    private LocalDateTime receivedDate;

    private OrderStatus status;

    
    
    // Constructors
    public Order() {}

    public Order(Customer customer, OrderStatus status, DeliveryDriver deliveryDriver) {
        this.customer = customer;
        this.cart = customer.getCart();  // Assuming Cart is a property of Customer
        this.status = status;
       
        this.orderDate = LocalDateTime.now();
        this.deliveryDriver =  deliveryDriver;
    }
    

    // Getters and Setters
    public UUID getId() { return id; }
    public Customer getCustomer() { return customer; }
    public Cart getCart() { return cart; }
    public OrderStatus getStatus() { return status; }
    public Double getTotal() { return total; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public LocalDateTime getReceivedDate() { return receivedDate; }
    public DeliveryDriver getDriver(){ return deliveryDriver;}

    public void setCustomer(Customer customer) { this.customer = customer; }
    public void setCart(Cart cart) { this.cart = cart; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public void setTotal(Double total) { this.total = total; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public void setReceivedDate(LocalDateTime receivedDate) { this.receivedDate = receivedDate; }
    public void setDeliveryDriver(DeliveryDriver deliveryDriver) { this.deliveryDriver = deliveryDriver; }

    @Override
    public String toString() {
        return String.format("Order{id=%s, customer=%s, total=%.2f, status=%s}", 
                id, customer.getName(), total, status);
    }
}
