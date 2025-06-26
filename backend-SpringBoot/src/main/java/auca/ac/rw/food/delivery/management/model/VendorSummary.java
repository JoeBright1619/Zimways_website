package auca.ac.rw.food.delivery.management.model;

import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public class VendorSummary {

    private UUID vendorId;
    private String name;
    private String type;   // or enum → store as STRING

    protected VendorSummary() {}            // JPA needs it

    public VendorSummary(UUID vendorId, String name, String type) {
        this.vendorId = vendorId;
        this.name     = name;
        this.type     = type;
    }

    /* getters (and setters only if you really need them) */
    public UUID getVendorId() { return vendorId; }
    public String getName()   { return name; }
    public String getType()   { return type; }
}