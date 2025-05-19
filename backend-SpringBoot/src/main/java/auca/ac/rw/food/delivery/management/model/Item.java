package auca.ac.rw.food.delivery.management.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity

public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    private String description;

    private String imageUrl;

    private double averageRating = 0.0;

    private int totalRatings = 0;

    private int reviewCount = 0;

    @Column(nullable = false)
    private boolean isAvailable = true;

    @Column(nullable = false)
    private double discountPercentage = 0.0;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", referencedColumnName = "id")
    @JsonManagedReference
    private Vendor vendor;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
    name = "item_category",  // Join table name
    joinColumns = @JoinColumn(name = "item_id"),  // Foreign key to the Item table
    inverseJoinColumns = @JoinColumn(name = "category_id")  // Foreign key to the Category table
    )
    @JsonManagedReference
    private List<Category> categories;

    // Constructors
    public Item() {}

    public Item(String name, double price, List<Category> categories, String description, Vendor vendor) {
        this.name = name;
        this.price = price;
        this.categories = categories;
        this.description = description;
        this.vendor = vendor;
    }

    // Getters and Setters

    public UUID getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public double getAverageRating() { return averageRating; }
    public int getTotalRatings() { return totalRatings; }
    public int getReviewCount() { return reviewCount; }
    public double getDiscountPercentage() { return discountPercentage; }
    public boolean isAvailable() { return isAvailable; }
    public List<Category> getCategories() { return categories; }
    public Vendor getVendor() { return vendor; }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setCategories(List<Category> categories) { this.categories = categories; }
    public void setVendor(Vendor vendor) { this.vendor = vendor; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public void setTotalRatings(int totalRatings) { this.totalRatings = totalRatings; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }
    public void setDiscountPercentage(double discountPercentage) { this.discountPercentage = discountPercentage; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", categories=" + categories +
                ", vendor=" + vendor +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        return id != null && id.equals(item.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
