package auca.ac.rw.food.delivery.management.repository;

import auca.ac.rw.food.delivery.management.model.Cart;
import auca.ac.rw.food.delivery.management.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByCustomer(Customer customer); // ✅ Add this method
     // ✅ Save a cart (Explicitly added, but JpaRepository already provides this)
     Cart save(Cart cart);

     // ✅ Find all carts (Explicitly added for clarity)
     List<Cart> findAll();
 
     // ✅ Find a cart by ID (Explicitly added for clarity)
     Optional<Cart> findById(UUID id);
 
     // ✅ Delete a cart by ID (Explicitly added for clarity)
     void deleteById(UUID id);
}
