package auca.ac.rw.food.delivery.management.model;

import jakarta.persistence.*;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore // ⛔ Don't serialize back to Cart
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private int quantity;
    private double totalPrice;

    public CartItem() {}

    public CartItem(Cart cart, Item item, int quantity) {
        this.cart = cart;
        this.item = item;
        this.quantity = quantity;
        this.totalPrice = item.getPrice() * quantity;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public Cart getCart() { return cart; }
    public Item getItem() { return item; }
    public int getQuantity() { return quantity; }
    public double getTotalPrice() { return totalPrice; }  // Assuming price is in the Item class

    public void setCart(Cart cart) { this.cart = cart; }
    public void setItem(Item item) { this.item = item; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }  // Assuming price is in the Item class
}
