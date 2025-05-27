package auca.ac.rw.food.delivery.management.model;

import auca.ac.rw.food.delivery.management.model.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

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
    @JsonIgnore // ✅ To avoid circular reference when serializing Customer -> Orders -> Customer...
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore // ✅ Cart may reference Order or Customer again
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    @JsonIgnore // ✅ DeliveryDriver might have a list of Orders
    private DeliveryDriver deliveryDriver;

    private Double total;
    private LocalDateTime orderDate;
    private LocalDateTime receivedDate;
    
    @Column(columnDefinition = "TEXT")
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    // Constructors
    public Order() {
        this.status = OrderStatus.PENDING;
        this.orderDate = LocalDateTime.now();
    }

    public Order(Customer customer, Cart cart, DeliveryDriver deliveryDriver) {
        this.customer = customer;
        this.cart = cart;
        this.deliveryDriver = deliveryDriver;
        this.status = OrderStatus.PENDING;
        this.orderDate = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public Customer getCustomer() { return customer; }
    public Cart getCart() { return cart; }
    public OrderStatus getStatus() { return status; }
    public Double getTotal() { return total; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public LocalDateTime getReceivedDate() { return receivedDate; }
    public DeliveryDriver getDeliveryDriver() { return deliveryDriver; }
    public String getDeliveryAddress() { return deliveryAddress; }

    public void setCustomer(Customer customer) { this.customer = customer; }
    public void setCart(Cart cart) { this.cart = cart; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public void setTotal(Double total) { this.total = total; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public void setReceivedDate(LocalDateTime receivedDate) { this.receivedDate = receivedDate; }
    public void setDeliveryDriver(DeliveryDriver deliveryDriver) { this.deliveryDriver = deliveryDriver; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    @Override
    public String toString() {
        return String.format("Order{id=%s, customer=%s, total=%.2f, status=%s}", 
                id, customer != null ? customer.getName() : "null", total, status);
    }
}
