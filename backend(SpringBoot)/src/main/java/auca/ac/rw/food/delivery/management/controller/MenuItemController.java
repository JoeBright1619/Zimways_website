package auca.ac.rw.food.delivery.management.controller;

import auca.ac.rw.food.delivery.management.model.Category;
import auca.ac.rw.food.delivery.management.model.MenuItem;
import auca.ac.rw.food.delivery.management.service.CategoryService;
import auca.ac.rw.food.delivery.management.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {

    private MenuItemService menuItemService;
    private CategoryService categoryService;

    @Autowired
    public MenuItemController(MenuItemService menuItemService, CategoryService categoryService) {
        this.menuItemService = menuItemService;
        this.categoryService = categoryService;
    }
    // Get all menu items
    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        List<MenuItem> menuItems = menuItemService.getAllMenuItems();
        return ResponseEntity.ok(menuItems);
    }

    // Get a menu item by ID
    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable UUID id) {
        Optional<MenuItem> menuItem = menuItemService.getMenuItemById(id);
        return menuItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Get a menu item by name
    @GetMapping("/name/{name}")
    public ResponseEntity<MenuItem> getMenuItemByName(@PathVariable String name) {
        Optional<MenuItem> menuItem = menuItemService.getMenuItemByName(name);
        return menuItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Get menu items by category
    @GetMapping("/category/{name}")
    public ResponseEntity<List<MenuItem>> getMenuItemsByCategory(@PathVariable String name) {
        // Fetch the Category from the database
        Optional<Category> categoryOptional = categoryService.getCategoryByName(name);
        
        if (categoryOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Category not found
        }
    
        Category category = categoryOptional.get(); 
    
        // Now fetch the MenuItems by the actual Category object
        List<MenuItem> menuItems = menuItemService.getMenuItemsByCategory(category);
        return ResponseEntity.ok(menuItems);
    }

    // Get menu items cheaper than a given price
    @GetMapping("/cheaper-than/{price}")
    public ResponseEntity<List<MenuItem>> getMenuItemsCheaperThan(@PathVariable double price) {
        List<MenuItem> menuItems = menuItemService.getMenuItemsCheaperThan(price);
        return ResponseEntity.ok(menuItems);
    }

    // Get menu items more expensive than a given price
    @GetMapping("/more-expensive-than/{price}")
    public ResponseEntity<List<MenuItem>> getMenuItemsMoreExpensiveThan(@PathVariable double price) {
        List<MenuItem> menuItems = menuItemService.getMenuItemsMoreExpensiveThan(price);
        return ResponseEntity.ok(menuItems);
    }

    // Search menu items by keyword in description
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<MenuItem>> searchMenuItemsByKeyword(@PathVariable String keyword) {
        List<MenuItem> menuItems = menuItemService.searchMenuItemsByKeyword(keyword);
        return ResponseEntity.ok(menuItems);
    }

    // Create a new menu item
    @PostMapping
    public ResponseEntity<MenuItem> createMenuItem(@RequestBody MenuItem menuItem) {
        MenuItem createdMenuItem = menuItemService.createMenuItem(menuItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMenuItem);
    }

    // Update an existing menu item
    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable UUID id, @RequestBody MenuItem updatedMenuItem) {
        MenuItem updatedItem = menuItemService.updateMenuItem(id, updatedMenuItem);
        return ResponseEntity.ok(updatedItem);
    }

    // Delete a menu item by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable UUID id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Delete a menu item by name
    @DeleteMapping("/name/{name}")
    public ResponseEntity<Void> deleteMenuItemByName(@PathVariable String name) {
        menuItemService.deleteMenuItemByName(name);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
