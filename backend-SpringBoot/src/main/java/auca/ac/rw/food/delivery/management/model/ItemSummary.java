package auca.ac.rw.food.delivery.management.model;

import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class ItemSummary {
    private UUID productId;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private UUID vendorId;
    private String vendorName;

    protected ItemSummary() {} // for JPA

    public ItemSummary(UUID productId, String name, String description,
                       double price, int quantity, UUID vendorId, String vendorName) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.vendorId = vendorId;
        this.vendorName = vendorName;
    }

    // getters only
    public UUID getProductId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public UUID getVendorId() { return vendorId; }
    public String getVendorName() { return vendorName; }
}
