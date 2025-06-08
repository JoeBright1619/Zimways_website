package auca.ac.rw.food.delivery.management.model;

import jakarta.persistence.*;
import java.util.UUID;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(
    name = "customer",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email", "name"}),
        @UniqueConstraint(columnNames = {"firebaseUid"})
    }
)
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
    
    @Column(nullable = false)
    private String password;

    private String address;

    @Column(name = "firebase_uid", nullable = false, unique = true)
    private String firebaseUid;

    @Column(name = "profile_url")
    private String profileUrl;

    @Transient // This field won't be persisted as it's only used for authentication
    private String firebaseToken;

    @Column(name = "tfa_secret")
    private String tfaSecret;

    @Column(name = "tfa_enabled", nullable = false)
    private boolean tfaEnabled = false;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Order> orders = new HashSet<>();

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Cart cart;

    public Customer() {
        this.tfaEnabled = false;
        this.orders = new HashSet<>();
    }

    public Customer(String name, String email, String phone, String address, String password, String firebaseUid) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.firebaseUid = firebaseUid; // This is required and will be stored
        this.tfaEnabled = false;
        this.orders = new HashSet<>();
    }

    public UUID getId() {        return id;    }

    public String getName() {        return name;    }

    public String getEmail() {        return email;    }

    public String getPhone() {        return phone;    }

    public String getAddress() {    return address;    }

    public String getPassword() {        return password;    }
    

    public Cart getCart() { return cart; }

    public String getTfaSecret() {
        return tfaSecret;
    }

    public void setTfaSecret(String tfaSecret) {
        this.tfaSecret = tfaSecret;
    }

    public boolean isTfaEnabled() {
        return tfaEnabled;
    }

    public void setTfaEnabled(boolean tfaEnabled) {
        this.tfaEnabled = tfaEnabled;
    }

    public void setName(String name) {        this.name = name;    }

    public void setEmail(String email) {        this.email = email;    }

    public void setPhone(String phone) {        this.phone = phone;    }
    public void setAddress(String address){    this.address = address;    }
    public void setPassword(String password) {        this.password = password;    }
    public void setCart(Cart cart) { this.cart = cart; }

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        if (firebaseUid == null || firebaseUid.trim().isEmpty()) {
            throw new IllegalArgumentException("Firebase UID cannot be null or empty");
        }
        this.firebaseUid = firebaseUid;
    }

    // Token is transient - only used for authentication, not stored
    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
