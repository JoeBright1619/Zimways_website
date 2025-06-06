package auca.ac.rw.food.delivery.management.DTO;

import auca.ac.rw.food.delivery.management.model.Category;
import auca.ac.rw.food.delivery.management.model.Item;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemResponseDTO {
    private UUID id;
    private String name;
    private double price;
    private String description;
    private String imageUrl;
    private boolean isAvailable;
    private double discountPercentage;
    private double averageRating;
    private int totalRatings;
    private int reviewCount;
    private String vendorName;
    private List<String> categoryNames;

    public ItemResponseDTO(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.description = item.getDescription();
        this.imageUrl = item.getImageUrl();
        this.isAvailable = item.isAvailable();
        this.discountPercentage = item.getDiscountPercentage();
        this.averageRating = item.getAverageRating();
        this.totalRatings = item.getTotalRatings();
        this.reviewCount = item.getReviewCount();
        this.vendorName = item.getVendor() != null ? item.getVendor().getName() : null;
        this.categoryNames = item.getCategories()
                         .stream()
                         .map(c -> c.getName().name())
                         .toList();

    }

    // Getters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public boolean isAvailable() { return isAvailable; }
    public double getDiscountPercentage() { return discountPercentage; }
    public double getAverageRating() { return averageRating; }
    public int getTotalRatings() { return totalRatings; }
    public int getReviewCount() { return reviewCount; }
    public String getVendorName() { return vendorName; }
    public List<String> getCategoryNames() { return categoryNames; }
}
