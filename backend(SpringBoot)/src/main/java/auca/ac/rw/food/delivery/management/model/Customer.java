package auca.ac.rw.food.delivery.management.model;

import jakarta.persistence.*;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String phone;
    
    private String address;


    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Cart cart;

    public Customer() {}

    public Customer(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public UUID getId() {        return id;    }

    public String getName() {        return name;    }

    public String getEmail() {        return email;    }

    public String getPhone() {        return phone;    }

    public String getAddress() {    return address;    }

    public Cart getCart() { return cart; }

    public void setName(String name) {        this.name = name;    }

    public void setEmail(String email) {        this.email = email;    }

    public void setPhone(String phone) {        this.phone = phone;    }
    public void setAddress(String address){    this.address = address;    }
    public void setCart(Cart cart) { this.cart = cart; }
}
