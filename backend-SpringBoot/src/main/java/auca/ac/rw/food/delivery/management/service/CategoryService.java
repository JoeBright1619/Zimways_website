package auca.ac.rw.food.delivery.management.service;


import auca.ac.rw.food.delivery.management.model.Category;
import auca.ac.rw.food.delivery.management.model.Vendor;
import auca.ac.rw.food.delivery.management.model.Item;
import auca.ac.rw.food.delivery.management.repository.CategoryRepository;
import auca.ac.rw.food.delivery.management.model.enums.ItemCategory;
import auca.ac.rw.food.delivery.management.service.ItemService;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ItemService itemService;

    public CategoryService(CategoryRepository categoryRepository, ItemService itemService) {
        this.categoryRepository = categoryRepository;
        this.itemService = itemService;
    }

    public Optional<Category> getCategoryByName(ItemCategory name){
        return categoryRepository.findByName(name);
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(UUID id){
        return categoryRepository.findById(id);
    }

    public List<ItemCategory> getCategoriesByVendorId(UUID vendorId) {
    List<Item> items = itemService.getItemsByVendorId(vendorId);

    if (items.isEmpty()) {
        return Collections.emptyList(); // Return an empty list if vendor has no items
    }

    return items.stream()
        .flatMap(item -> item.getCategories().stream()) // get all categories from all items
        .map(Category::getName) // get the enum name (ItemCategory)
        .distinct() // avoid duplicates
        .collect(Collectors.toList()); // return the unique list
}


    public Category saveCategory(Category category){

        Optional<Category> existingCategory = categoryRepository
    .findByName(category.getName());

    if (existingCategory.isPresent()) {
    throw new IllegalStateException("Category with the same name already exist.");
    }
        return categoryRepository.save(category);
    }
    public void deleteCategoryByName(ItemCategory name){
        categoryRepository.deleteByName(name);
    }
}
