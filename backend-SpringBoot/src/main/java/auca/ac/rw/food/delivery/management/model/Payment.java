package auca.ac.rw.food.delivery.management.model;

import auca.ac.rw.food.delivery.management.model.enums.PaymentMethod;
import auca.ac.rw.food.delivery.management.model.enums.PaymentStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    @Column(nullable = false, columnDefinition = "timestamp without time zone")
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id", unique = true)
    
    private Order order;

    // Constructors
    public Payment() {
        this.paymentDate = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
    }

    public Payment(double amount, PaymentMethod paymentMethod, Order order) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.order = order;
        this.paymentDate = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public double getAmount() { return amount; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public Order getOrder() { return order; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public PaymentStatus getStatus() { return status; }

    public void setAmount(double amount) { this.amount = amount; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setOrder(Order order) { this.order = order; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Payment{id=%s, amount=%.2f, method=%s, status=%s}", 
                id, amount, paymentMethod, status);
    }

    @PreRemove
    private void preRemove() {
        // Detach from order before removal
        if (order != null) {
            if (order.getPayment() != null) {
                order.setPayment(null);
            }
            this.order = null;
        }
    }
}
