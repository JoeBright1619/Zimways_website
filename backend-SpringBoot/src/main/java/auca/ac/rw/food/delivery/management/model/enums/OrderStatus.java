package auca.ac.rw.food.delivery.management.model.enums;

public enum OrderStatus {
    // Initial states
    PENDING("Pending"),                    // Order received but not yet confirmed by restaurant
    CONFIRMED("Confirmed"),                // Restaurant has confirmed the order
    PREPARING("Preparing"),               // Restaurant is preparing the food
    
    // Payment states
    PAYMENT_PENDING("Payment Pending"),    // Waiting for payment
    PAYMENT_PROCESSING("Payment Processing"), // Payment is being processed
    PAYMENT_COMPLETED("Payment Completed"), // Payment successful
    PAYMENT_FAILED("Payment Failed"),      // Payment failed
    
    // Delivery states
    READY_FOR_PICKUP("Ready for Pickup"),  // Food is ready for driver pickup
    DRIVER_ASSIGNED("Driver Assigned"),    // Driver has been assigned
    DRIVER_PICKED_UP("Driver Picked Up"),  // Driver has picked up the order
    OUT_FOR_DELIVERY("Out for Delivery"),  // Driver is delivering the order
    
    // Completion states
    DELIVERED("Delivered"),                // Order has been delivered
    COMPLETED("Completed"),                // Order is fully completed (delivered and payment confirmed)
    
    // Cancellation states
    CANCELLED_BY_CUSTOMER("Cancelled by Customer"), // Customer cancelled the order
    CANCELLED_BY_RESTAURANT("Cancelled by Restaurant"), // Restaurant cancelled the order
    CANCELLED_BY_SYSTEM("Cancelled by System"), // System cancelled (e.g., no driver available)
    REFUNDED("Refunded");                  // Order was refunded

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    // Helper methods to check status categories
    public boolean isInitialState() {
        return this == PENDING || this == CONFIRMED || this == PREPARING;
    }

    public boolean isPaymentState() {
        return this == PAYMENT_PENDING || this == PAYMENT_PROCESSING || 
               this == PAYMENT_COMPLETED || this == PAYMENT_FAILED;
    }

    public boolean isDeliveryState() {
        return this == READY_FOR_PICKUP || this == DRIVER_ASSIGNED || 
               this == DRIVER_PICKED_UP || this == OUT_FOR_DELIVERY;
    }

    public boolean isCompletionState() {
        return this == DELIVERED || this == COMPLETED;
    }

    public boolean isCancellationState() {
        return this == CANCELLED_BY_CUSTOMER || this == CANCELLED_BY_RESTAURANT || 
               this == CANCELLED_BY_SYSTEM || this == REFUNDED;
    }

    public boolean canTransitionTo(OrderStatus newStatus) {
        // Define valid state transitions
        switch (this) {
            case PENDING:
                return newStatus == CONFIRMED || newStatus == CANCELLED_BY_CUSTOMER || 
                       newStatus == CANCELLED_BY_RESTAURANT;
            case CONFIRMED:
                return newStatus == PREPARING || newStatus == PAYMENT_PENDING || 
                       newStatus == CANCELLED_BY_RESTAURANT;
            case PREPARING:
                return newStatus == READY_FOR_PICKUP || newStatus == PAYMENT_PENDING || 
                       newStatus == CANCELLED_BY_RESTAURANT;
            case PAYMENT_PENDING:
                return newStatus == PAYMENT_PROCESSING || newStatus == CANCELLED_BY_CUSTOMER;
            case PAYMENT_PROCESSING:
                return newStatus == PAYMENT_COMPLETED || newStatus == PAYMENT_FAILED;
            case PAYMENT_COMPLETED:
                return newStatus == READY_FOR_PICKUP;
            case PAYMENT_FAILED:
                return newStatus == PAYMENT_PENDING || newStatus == CANCELLED_BY_SYSTEM;
            case READY_FOR_PICKUP:
                return newStatus == DRIVER_ASSIGNED || newStatus == CANCELLED_BY_RESTAURANT;
            case DRIVER_ASSIGNED:
                return newStatus == DRIVER_PICKED_UP || newStatus == CANCELLED_BY_SYSTEM;
            case DRIVER_PICKED_UP:
                return newStatus == OUT_FOR_DELIVERY;
            case OUT_FOR_DELIVERY:
                return newStatus == DELIVERED || newStatus == CANCELLED_BY_SYSTEM;
            case DELIVERED:
                return newStatus == COMPLETED;
            case COMPLETED:
                return newStatus == REFUNDED;
            case CANCELLED_BY_CUSTOMER:
            case CANCELLED_BY_RESTAURANT:
            case CANCELLED_BY_SYSTEM:
            case REFUNDED:
                return false; // Terminal states
            default:
                return false;
        }
    }
}