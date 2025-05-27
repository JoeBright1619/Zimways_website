package auca.ac.rw.food.delivery.management.DTO;

import java.util.List;
import java.util.UUID;

public class OrderDTO {
    private UUID customerId;
    private UUID driverId;
    private String deliveryAddress;
    private List<OrderItemDTO> items;

    // Inner class for order items
    public static class OrderItemDTO {
        private UUID itemId;
        private int quantity;

        public UUID getItemId() { return itemId; }
        public void setItemId(UUID itemId) { this.itemId = itemId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    // Getters and Setters
    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }

    public UUID getDriverId() { return driverId; }
    public void setDriverId(UUID driverId) { this.driverId = driverId; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
}
