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

    private String icon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryType type = CategoryType.PRODUCT;  // Default to ITEM for backward compatibility

    // Many Categories can have many Items
    @ManyToMany(mappedBy = "categories")
    @JsonBackReference(value = "category-items")
    private Set<Item> items = new HashSet<>();

    @ManyToMany(mappedBy = "categories")
    @JsonBackReference(value = "category-vendors")
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
    public Set<Item> getItems() { return items; }
    public String getIcon() { return icon; }
    public Set<Vendor> getVendors() { return vendors; }
    public CategoryType getType() { return type; }


    public void setName(ItemCategory name) { this.name = name; }
    public void setItems(Set<Item> items) { this.items = items; }
    public void setIcon(String icon) { this.icon = icon; }
    public void setVendors(Set<Vendor> vendors) { this.vendors = vendors; }
    public void setType(CategoryType type) { this.type = type; }
}
