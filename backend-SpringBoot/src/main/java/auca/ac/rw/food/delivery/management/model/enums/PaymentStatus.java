package auca.ac.rw.food.delivery.management.model.enums;

public enum PaymentStatus {
    PENDING,    // Payment initiated but not yet processed
    PROCESSING, // Payment is being processed
    COMPLETED,  // Payment successfully completed
    FAILED,     // Payment failed
    REFUNDED,   // Payment was refunded
    CANCELLED   // Payment was cancelled
} 