package auca.ac.rw.food.delivery.management.service;


import auca.ac.rw.food.delivery.management.model.Category;
import auca.ac.rw.food.delivery.management.model.Restaurant;
import auca.ac.rw.food.delivery.management.repository.CategoryRepository;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Optional<Category> getCategoryByName(String name){
        return categoryRepository.findByName(name);
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(UUID id){
        return categoryRepository.findById(id);
    }

    public Category saveCategory(Category category){

        Optional<Category> existingCategory = categoryRepository
    .findByName(category.getName());

    if (existingCategory.isPresent()) {
    throw new IllegalStateException("Category with the same name already exist.");
    }
        return categoryRepository.save(category);
    }
    public void deleteCategoryByName(String name){
        categoryRepository.deleteByName(name);
    }
}
