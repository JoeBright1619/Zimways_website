package auca.ac.rw.food.delivery.management.model;


import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private double amount;
    private String paymentMethod; // Cash, Card, Mobile Money
    private String paymentDate;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    // Constructors
    public Payment() {}

    public Payment(double amount, String paymentMethod, String paymentDate,Order order) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.order = order;
        this.paymentDate = paymentDate;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public double getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public Order getOrder() { return order; }
    public String getPaymentDate() { return paymentDate; }

    public void setAmount(double amount) { this.amount = amount; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setOrder(Order order) { this.order = order; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }
}
