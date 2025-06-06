package auca.ac.rw.food.delivery.management.service;

import auca.ac.rw.food.delivery.management.model.Category;
import auca.ac.rw.food.delivery.management.model.Item;
import auca.ac.rw.food.delivery.management.model.Vendor;
import auca.ac.rw.food.delivery.management.model.enums.ItemCategory;
import auca.ac.rw.food.delivery.management.repository.CategoryRepository;
import auca.ac.rw.food.delivery.management.repository.ItemRepository;
import auca.ac.rw.food.delivery.management.repository.VendorRepository;
import auca.ac.rw.food.delivery.management.DTO.ItemCreationDTO;
import auca.ac.rw.food.delivery.management.DTO.ItemResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemService {
      @Autowired
        private ItemRepository itemRepository;

        @Autowired
        private VendorRepository vendorRepository;

        @Autowired
        private CategoryRepository categoryRepository;

    public ItemService(ItemRepository itemRepository,
                       VendorRepository vendorRepository,
                       CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.vendorRepository = vendorRepository;
        this.categoryRepository = categoryRepository;
    }

    // ✅ Get all items
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // ✅ Get an item by ID
    public Optional<ItemResponseDTO> getItemById(UUID id) {

        return itemRepository.findById(id)
                .map(ItemResponseDTO::new); 
    }

    // ✅ Get an item by name
    public Optional<Item> getItemByName(String name) {
        return itemRepository.findByName(name);
    }

    // ✅ Get items under a specific category
    public List<Item> getItemsByCategoryName(String categoryName) {
        return itemRepository.findByCategoryName(categoryName);
    }

    // ✅ Get items cheaper than a certain price
    public List<Item> getItemsCheaperThan(double price) {
        return itemRepository.findByPriceLessThan(price);
    }

    // ✅ Get items more expensive than a certain price
    public List<Item> getItemsMoreExpensiveThan(double price) {
        return itemRepository.findByPriceGreaterThan(price);
    }

    // ✅ Search items by keyword in description
    public List<Item> searchItemsByKeyword(String keyword) {
        return itemRepository.findByDescriptionContainingIgnoreCase(keyword);
    }

    // ✅ Get items belonging to a specific vendor
    public List<Item> getItemsByVendor(String vendorName) {
        return itemRepository.findByVendorName(vendorName);
    }

    public List<Item> getItemsByVendorId(UUID vendorId) {
        return itemRepository.findByVendorId(vendorId);
    }

    // ✅ Create a new item
    public Item createItem(ItemCreationDTO item) {
        Vendor vendor = vendorRepository.findByNameIgnoreCase(item.getVendorName())
            .orElseThrow(() -> new IllegalArgumentException("Vendor '" + item.getVendorName() + "' not found"));
    
        Optional<Item> existingItem = itemRepository.findByVendorAndName(vendor, item.getName());
        if (existingItem.isPresent()) {
            throw new IllegalArgumentException("Item with this name already exists for the vendor");
        }
            
        // Fetch categories
        List<Category> categories = categoryRepository.findByNameIn(item.getCategoryNames());

        // Check if all categories were found
        if (categories.size() != item.getCategoryNames().size()) {
            List<ItemCategory> foundNames = categories.stream().map(Category::getName).toList();
            List<String> missing = item.getCategoryNames().stream()
                    .filter(name -> !foundNames.contains(name))
                    .toList();
            throw new IllegalArgumentException("Categories not found: " + String.join(", ", missing));
        }

        Item newItem = new Item();
        newItem.setName(item.getName());
        newItem.setPrice(item.getPrice());
        newItem.setDescription(item.getDescription());
        newItem.setImageUrl(item.getImageUrl());
        
        // Set availability explicitly
        System.out.println("Setting initial isAvailable to: " + item.isAvailable());
        newItem.setAvailable(item.isAvailable());
        
        newItem.setDiscountPercentage(item.getDiscountPercentage());
        newItem.setVendor(vendor);
        newItem.setCategories(categories);
        
        Item savedItem = itemRepository.save(newItem);
        System.out.println("Saved new item isAvailable: " + savedItem.isAvailable());
        return savedItem;
    }

    // ✅ Update an existing item
    public Item updateItem(UUID id, ItemCreationDTO updatedItem) {
        return itemRepository.findById(id)
                .map(existingItem -> {
                    if (updatedItem.getName() != null && !updatedItem.getName().trim().isEmpty()) {
                        existingItem.setName(updatedItem.getName().trim());
                    }

                    if (updatedItem.getPrice() >= 0) {
                        existingItem.setPrice(updatedItem.getPrice());
                    }

                    if (updatedItem.getDescription() != null) {
                        existingItem.setDescription(updatedItem.getDescription().trim());
                    }

                    if (updatedItem.getImageUrl() != null) {
                        existingItem.setImageUrl(updatedItem.getImageUrl().trim());
                    }

                    // Always update isAvailable as it's a boolean
                    System.out.println("Updating isAvailable from " + existingItem.isAvailable() + " to " + updatedItem.isAvailable());
                    existingItem.setAvailable(updatedItem.isAvailable());

                    if (updatedItem.getDiscountPercentage() >= 0) {
                        existingItem.setDiscountPercentage(updatedItem.getDiscountPercentage());
                    }

                    // Handle category update if provided
                    if (updatedItem.getCategoryNames() != null && !updatedItem.getCategoryNames().isEmpty()) {
                        List<Category> categories = categoryRepository.findByNameIn(updatedItem.getCategoryNames());
                        if (!categories.isEmpty()) {
                            existingItem.setCategories(categories);
                        }
                    }

                    // Handle vendor update if provided
                    if (updatedItem.getVendorName() != null && !updatedItem.getVendorName().trim().isEmpty()) {
                        vendorRepository.findByNameIgnoreCase(updatedItem.getVendorName().trim()).ifPresent(existingItem::setVendor);
                    }

                    Item savedItem = itemRepository.save(existingItem);
                    System.out.println("Saved item isAvailable: " + savedItem.isAvailable());
                    return savedItem;
                })
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    // ✅ Delete an item by ID
    public void deleteItem(UUID id) {
        itemRepository.deleteById(id);
    }

    // ✅ Delete an item by name
    public void deleteItemByName(String name) {
        if (itemRepository.findByName(name).isEmpty()) {
            throw new RuntimeException("Item with name '" + name + "' not found");
        }
        itemRepository.deleteByName(name);
    }
}
