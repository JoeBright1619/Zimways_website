package auca.ac.rw.food.delivery.management.service;

import auca.ac.rw.food.delivery.management.model.Category;
import auca.ac.rw.food.delivery.management.model.Vendor;
import auca.ac.rw.food.delivery.management.model.Item;
import auca.ac.rw.food.delivery.management.repository.CategoryRepository;
import auca.ac.rw.food.delivery.management.model.enums.ItemCategory;
import auca.ac.rw.food.delivery.management.service.ItemService;
import auca.ac.rw.food.delivery.management.service.VendorService;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ItemService itemService;
    private final VendorService vendorService;

    public CategoryService(CategoryRepository categoryRepository, 
                         ItemService itemService,
                         VendorService vendorService) {
        this.categoryRepository = categoryRepository;
        this.itemService = itemService;
        this.vendorService = vendorService;
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
            return Collections.emptyList();
        }

        return items.stream()
            .flatMap(item -> item.getCategories().stream())
            .map(Category::getName)
            .distinct()
            .collect(Collectors.toList());
    }

    public Set<Item> getItemsByCategory(ItemCategory categoryName) {
        Optional<Category> category = categoryRepository.findByName(categoryName);
        if (category.isEmpty()) {
            return new HashSet<>();
        }
        return category.get().getItems();
    }

    public List<Vendor> getVendorsByCategory(ItemCategory categoryName) {
        Optional<Category> category = categoryRepository.findByName(categoryName);
        if (category.isEmpty()) {
            return Collections.emptyList();
        }
        return category.get().getVendors().stream().collect(Collectors.toList());
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
