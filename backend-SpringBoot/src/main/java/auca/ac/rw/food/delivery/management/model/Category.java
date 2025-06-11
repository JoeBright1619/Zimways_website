package auca.ac.rw.food.delivery.management.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import java.util.Set;
import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonBackReference;

import auca.ac.rw.food.delivery.management.model.enums.ItemCategory;
import auca.ac.rw.food.delivery.management.model.enums.CategoryType;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private ItemCategory name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryType type = CategoryType.ITEM;  // Default to ITEM for backward compatibility

    // Many Categories can have many Items
    @ManyToMany(mappedBy = "categories")  // mappedBy points to the field in the Item class
    @JsonBackReference
    private List<Item> items;  // This is the inverse side of the relationship

    // Many Categories can have many Vendors
    @ManyToMany(mappedBy = "categories")
    @JsonBackReference
    private Set<Vendor> vendors = new HashSet<>();

    // Constructors
    public Category() {}

    public Category(ItemCategory name) {
        this.name = name;
    }

    public Category(ItemCategory name, CategoryType type) {
        this.name = name;
        this.type = type;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public ItemCategory getName() { return name; }
    public List<Item> getItems() { return items; }
    public Set<Vendor> getVendors() { return vendors; }
    public CategoryType getType() { return type; }

    public void setName(ItemCategory name) { this.name = name; }
    public void setItems(List<Item> items) { this.items = items; }
    public void setVendors(Set<Vendor> vendors) { this.vendors = vendors; }
    public void setType(CategoryType type) { this.type = type; }
}
