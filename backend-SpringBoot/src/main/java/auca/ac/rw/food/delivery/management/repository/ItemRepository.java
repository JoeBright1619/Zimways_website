package auca.ac.rw.food.delivery.management.repository;

import auca.ac.rw.food.delivery.management.model.Category;
import auca.ac.rw.food.delivery.management.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {

    // ✅ Find items by name
    Optional<Item> findByName(String name);

    Optional<Item> findById(UUID id);

    // ✅ Get all items under a specific category
    @Query("SELECT i FROM Item i JOIN i.categories c WHERE c.name = :categoryName")
    List<Item> findByCategoryName(String categoryName);

    List<Item> findAll();

    // ✅ Find items cheaper than a certain price
    List<Item> findByPriceLessThan(double price);

     // ✅ Find items greater than a certain price
    List<Item> findByPriceGreaterThan(double price);
    // ✅ Find items containing a keyword in description
    List<Item> findByDescriptionContainingIgnoreCase(String keyword);

    List<Item> findByVendorId(UUID id);
    // ✅ Find items by vendor name
    List<Item> findByVendorName(String vendorName);

    // ✅ Custom delete method by name
    void deleteByName(String name);

    void deleteById(UUID id);
   
    Item save(Item Item);
    

}
