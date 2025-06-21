package auca.ac.rw.food.delivery.management.repository;

import auca.ac.rw.food.delivery.management.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import auca.ac.rw.food.delivery.management.model.enums.ItemCategory;
import auca.ac.rw.food.delivery.management.model.enums.CategoryType;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
   Optional<Category> findByName(ItemCategory name);  // ✅ Find category by name
   Set<Category> findByNameIn(List<String> names); // ✅ Find categories by a list of names
   List<Category> findAll();
   List<Category> findByType(CategoryType type); // ✅ Find categories by type
   Category save(Category category);


   void deleteByName(ItemCategory name);
   Optional<Category> findById(UUID id);
}
