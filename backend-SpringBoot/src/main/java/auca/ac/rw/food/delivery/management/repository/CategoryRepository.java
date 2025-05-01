package auca.ac.rw.food.delivery.management.repository;

import auca.ac.rw.food.delivery.management.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
   Optional<Category> findByName(String name);  // ✅ Find category by name
   List<Category> findAll();
   Category save(Category category);
   
   void deleteByName(String name);
   Optional<Category> findById(UUID id);
}
