package auca.ac.rw.food.delivery.management.DTO;
import java.util.UUID;


public class OrderDTO {
    private UUID customerId;
    private UUID driverId;

    // Getters and Setters
    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }

    public UUID getDriverId() { return driverId; }
    public void setDriverId(UUID driverId) { this.driverId = driverId; }
}
