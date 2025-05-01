package auca.ac.rw.food.delivery.management.service;

import auca.ac.rw.food.delivery.management.model.Category;
import auca.ac.rw.food.delivery.management.model.MenuItem;
import auca.ac.rw.food.delivery.management.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    // ✅ Get all menu items
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    // ✅ Get a menu item by ID
    public Optional<MenuItem> getMenuItemById(UUID id) {
        return menuItemRepository.findById(id);
    }

    // ✅ Get a menu item by name
    public Optional<MenuItem> getMenuItemByName(String name) {
        return menuItemRepository.findByName(name);
    }

    // ✅ Get menu items under a specific category
    public List<MenuItem> getMenuItemsByCategory(Category category) {
        return menuItemRepository.findByCategory(category);
    }

    // ✅ Get menu items cheaper than a certain price
    public List<MenuItem> getMenuItemsCheaperThan(double price) {
        return menuItemRepository.findByPriceLessThan(price);
    }

    // ✅ Get menu items more expensive than a certain price
    public List<MenuItem> getMenuItemsMoreExpensiveThan(double price) {
        return menuItemRepository.findByPriceGreaterThan(price);
    }

    // ✅ Search menu items by keyword in description
    public List<MenuItem> searchMenuItemsByKeyword(String keyword) {
        return menuItemRepository.findByDescriptionContainingIgnoreCase(keyword);
    }

    // ✅ Get menu items belonging to a specific restaurant
    public List<MenuItem> getMenuItemsByRestaurant(UUID restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    // ✅ Create a new menu item
    public MenuItem createMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    // ✅ Update an existing menu item
    public MenuItem updateMenuItem(UUID id, MenuItem updatedMenuItem) {
        return menuItemRepository.findById(id)
                .map(existingItem -> {
                    existingItem.setName(updatedMenuItem.getName());
                    existingItem.setPrice(updatedMenuItem.getPrice());
                    existingItem.setDescription(updatedMenuItem.getDescription());
                    existingItem.setCategory(updatedMenuItem.getCategory());
                    return menuItemRepository.save(existingItem);
                }).orElseThrow(() -> new RuntimeException("Menu item not found"));
    }

    // ✅ Delete a menu item by ID
    public void deleteMenuItem(UUID id) {
        menuItemRepository.deleteById(id);
    }

    // ✅ Delete a menu item by name
    public void deleteMenuItemByName(String name) {
        if (menuItemRepository.findByName(name).isEmpty()) {
            throw new RuntimeException("Menu item with name '" + name + "' not found");
        }
        menuItemRepository.deleteByName(name);
    }
}
