package auca.ac.rw.food.delivery.management.model;

import auca.ac.rw.food.delivery.management.model.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @JsonIgnore // To avoid circular reference when serializing Customer -> Orders -> Customer...
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore // Cart may reference Order or Customer again
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

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Payment payment;

    @ElementCollection(fetch = FetchType.EAGER)           // snapshot list
    @CollectionTable(name = "order_vendor_summaries", joinColumns = @JoinColumn(name = "order_id"))
    private List<VendorSummary> vendors = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "order_item_summaries", joinColumns = @JoinColumn(name = "order_id"))
    private List<ItemSummary> items = new ArrayList<>();

    

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
    public Payment getPayment() { return payment; }

    public void setCustomer(Customer customer) { this.customer = customer; }
    public void setCart(Cart cart) { this.cart = cart; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public void setTotal(Double total) { this.total = total; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public void setReceivedDate(LocalDateTime receivedDate) { this.receivedDate = receivedDate; }
    public void setDeliveryDriver(DeliveryDriver deliveryDriver) { this.deliveryDriver = deliveryDriver; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public void setPayment(Payment payment) { this.payment = payment; }

    @Override
    public String toString() {
        return String.format("Order{id=%s, customer=%s, total=%.2f, status=%s}", 
                id, customer != null ? customer.getName() : "null", total, status);
    }

    @PreRemove
    private void preRemove() {
        // Detach from payment if exists
        if (payment != null) {
            payment.setOrder(null);
            this.payment = null;
        }

        // Detach from cart before removal
        if (cart != null) {
            cart.setOrder(null);
            this.cart = null;
        }
        
        // Detach from customer before removal
        if (customer != null) {
            customer.getOrders().remove(this);
            this.customer = null;
        }
    }
}
