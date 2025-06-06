package auca.ac.rw.food.delivery.management.controller;

import auca.ac.rw.food.delivery.management.model.Category;
import auca.ac.rw.food.delivery.management.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import auca.ac.rw.food.delivery.management.model.enums.ItemCategory;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
   

    // Get category by name

    @GetMapping()
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
return categories.isEmpty()
        ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        : ResponseEntity.ok(categories);
     }

   @GetMapping("/{name}")
public ResponseEntity<Category> getCategoryByName(@PathVariable String name) {
    try {
        ItemCategory categoryEnum = ItemCategory.valueOf(name.toUpperCase());
        Optional<Category> category = categoryService.getCategoryByName(categoryEnum);
        return category.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}
    @GetMapping("/vendors/{vendorId}/categories")
public ResponseEntity<?> getVendorCategories(@PathVariable UUID vendorId) {
    // logic here
    List<ItemCategory> categories = categoryService.getCategoriesByVendorId(vendorId);
    if (categories.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(categories);
}


    // Save a new category or update an existing category
    @PostMapping
    public ResponseEntity<Category> saveCategory(@RequestBody Category category) {
        Category savedCategory = categoryService.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    // Delete category by name
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteCategoryByName(@PathVariable String name) {
        categoryService.deleteCategoryByName(ItemCategory.valueOf(name.toUpperCase()));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
