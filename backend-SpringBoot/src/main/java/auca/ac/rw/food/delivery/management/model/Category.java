package auca.ac.rw.food.delivery.management.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import auca.ac.rw.food.delivery.management.model.enums.ItemCategory;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private ItemCategory name;

    // Many Categories can have many Items
    @ManyToMany(mappedBy = "categories")  // mappedBy points to the field in the Item class
    private List<Item> items;  // This is the inverse side of the relationship

    // Constructors
    public Category() {}

    public Category(ItemCategory name) {
        this.name = name;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public ItemCategory getName() { return name; }
    public List<Item> getItems() { return items; }

    public void setName(ItemCategory name) { this.name = name; }
    public void setItems(List<Item> items) { this.items = items; }
}
