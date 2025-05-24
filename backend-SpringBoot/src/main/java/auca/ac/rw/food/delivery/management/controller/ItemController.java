package auca.ac.rw.food.delivery.management.controller;

import auca.ac.rw.food.delivery.management.model.Category;
import auca.ac.rw.food.delivery.management.model.Item;
import auca.ac.rw.food.delivery.management.service.CategoryService;
import auca.ac.rw.food.delivery.management.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import auca.ac.rw.food.delivery.management.model.enums.ItemCategory;
import auca.ac.rw.food.delivery.management.DTO.ItemDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private ItemService itemService;
    private CategoryService categoryService;

    @Autowired
    public ItemController(ItemService itemService, CategoryService categoryService) {
        this.itemService = itemService;
        this.categoryService = categoryService;
    }

    // Get all items
    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<Item>> getItemsByVendor(@PathVariable UUID vendorId) {
        List<Item> items = itemService.getItemsByVendorId(vendorId);
        return ResponseEntity.ok(items);
    }

    // Get an item by ID
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable UUID id) {
        Optional<Item> item = itemService.getItemById(id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Get an item by name
    @GetMapping("/name/{name}")
    public ResponseEntity<Item> getItemByName(@PathVariable String name) {
        Optional<Item> item = itemService.getItemByName(name);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Get items by category
    @GetMapping("/category/{name}")
    public ResponseEntity<List<Item>> getItemsByCategory(@PathVariable String name) {
        List<Item> items = itemService.getItemsByCategoryName(name);
        return ResponseEntity.ok(items);
    }

    // Get items cheaper than a given price
    @GetMapping("/cheaper-than/{price}")
    public ResponseEntity<List<Item>> getItemsCheaperThan(@PathVariable double price) {
        List<Item> items = itemService.getItemsCheaperThan(price);
        return ResponseEntity.ok(items);
    }

    // Get items more expensive than a given price
    @GetMapping("/more-expensive-than/{price}")
    public ResponseEntity<List<Item>> getItemsMoreExpensiveThan(@PathVariable double price) {
        List<Item> items = itemService.getItemsMoreExpensiveThan(price);
        return ResponseEntity.ok(items);
    }

    // Search items by keyword in description
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Item>> searchItemsByKeyword(@PathVariable String keyword) {
        List<Item> items = itemService.searchItemsByKeyword(keyword);
        return ResponseEntity.ok(items);
    }

    // Create a new item
    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody ItemDTO item) {
        Item createdItem = itemService.createItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    // Update an existing item
    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable UUID id, @RequestBody ItemDTO updatedItem) {
        Item item = itemService.updateItem(id, updatedItem);
        return ResponseEntity.ok(item);
    }

    // Delete an item by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable UUID id) {
        itemService.deleteItem(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Delete an item by name
    @DeleteMapping("/name/{name}")
    public ResponseEntity<Void> deleteItemByName(@PathVariable String name) {
        itemService.deleteItemByName(name);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
