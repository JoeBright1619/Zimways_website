package auca.ac.rw.food.delivery.management.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double price;
    private String description;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    // Constructors
    public MenuItem() {}

    public MenuItem(String name, double price, Category category, String description, Restaurant restaurant) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.restaurant = restaurant;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public Restaurant getRestaurant() { return restaurant; }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setCategory(Category category) { this.category = category; }
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", category='" + category + '\'' +                
                '}';
    }

    @Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    MenuItem menuItem = (MenuItem) obj;
    return id != null && id.equals(menuItem.id);  // Compare by ID
}

@Override
public int hashCode() {
    return id != null ? id.hashCode() : 0;
}

}
