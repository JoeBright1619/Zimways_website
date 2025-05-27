package auca.ac.rw.food.delivery.management.DTO;

import java.util.List;
import java.util.UUID;

public class ItemDTO {
    private String name;
    private double price;
    private String description;
    private String imageUrl;
    private boolean isAvailable = true; // Default to true
    private double discountPercentage;
    private String vendorName;
    private List<String> categoryNames;

    // Default constructor
    public ItemDTO() {}

    // Getters
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public boolean isAvailable() { return isAvailable; }
    public double getDiscountPercentage() { return discountPercentage; }
    public String getVendorName() { return vendorName; }
    public List<String> getCategoryNames() { return categoryNames; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setAvailable(boolean available) { this.isAvailable = available; }
    public void setDiscountPercentage(double discountPercentage) { this.discountPercentage = discountPercentage; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }
    public void setCategoryNames(List<String> categoryNames) { this.categoryNames = categoryNames; }

    @Override
    public String toString() {
        return "ItemDTO{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", isAvailable=" + isAvailable +
                ", discountPercentage=" + discountPercentage +
                ", vendorName='" + vendorName + '\'' +
                ", categoryNames=" + categoryNames +
                '}';
    }
}

