package auca.ac.rw.food.delivery.management.repository;

import auca.ac.rw.food.delivery.management.model.Category;
import auca.ac.rw.food.delivery.management.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {

    // ✅ Find menu items by name
    Optional<MenuItem> findByName(String name);

    Optional<MenuItem> findById(UUID id);

    // ✅ Get all menu items under a specific category
    List<MenuItem> findByCategory(Category category);

    List<MenuItem> findAll();

    // ✅ Find menu items cheaper than a certain price
    List<MenuItem> findByPriceLessThan(double price);

     // ✅ Find menu items greater than a certain price
    List<MenuItem> findByPriceGreaterThan(double price);
    // ✅ Find menu items containing a keyword in description
    List<MenuItem> findByDescriptionContainingIgnoreCase(String keyword);

    List<MenuItem> findByRestaurantId(UUID id);

    // ✅ Custom delete method by name
    void deleteByName(String name);

    void deleteById(UUID id);
   
    MenuItem save(MenuItem menuItem);
    

}
