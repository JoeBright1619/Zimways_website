package auca.ac.rw.food.delivery.management.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL) // Cascade ensures the cart is deleted with the customer
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @JsonIgnore
    private Customer customer;

    @ManyToMany
    @JoinTable(
        name = "cart_items",
        joinColumns = @JoinColumn(name = "cart_id"),
        inverseJoinColumns = @JoinColumn(name = "menu_item_id")
    )
    private List<MenuItem> menuItems = new ArrayList<>();

    private Double amount = 0.0;  // Change from Integer to Double
    private Integer quantity = 0;

    // Constructors
    public Cart() {}

    public Cart(Customer customer) {
        this.customer = customer;
        this.menuItems = new ArrayList<>();
        this.amount = 0.0;
        this.quantity = 0;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public Customer getCustomer() { return customer; }
    public List<MenuItem> getMenuItems() { return menuItems; }
    public Double getAmount() { return amount; }  // Update getter
    public Integer getQuantity(){ return quantity; }

    public void setCustomer(Customer customer) { this.customer = customer; }
    public void setMenuItems(List<MenuItem> menuItems) { this.menuItems = menuItems; }
    public void setAmount(Double amount) { this.amount = amount; }  // Update setter
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
