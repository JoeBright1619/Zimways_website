package auca.ac.rw.food.delivery.management.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")

    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;

    @OneToOne(mappedBy = "cart")
    @JsonIgnore
    private Order order;

    public Cart() {}

    public Cart(Customer customer) {
        this.customer = customer;
        this.cartItems = new ArrayList<>();
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public Customer getCustomer() { return customer; }
    public List<CartItem> getCartItems() { return cartItems; }

    public void setCustomer(Customer customer) { this.customer = customer; }
    public void setCartItems(List<CartItem> cartItems) { this.cartItems = cartItems; }

    @PreRemove
    private void preRemove() {
        // Detach from order before removal
        if (order != null) {
            order.setCart(null);
            this.order = null;
        }
        // Detach from customer before removal
        if (customer != null) {
            customer.setCart(null);
            this.customer = null;
        }
    }

    // Add getter and setter for order
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
