package auca.ac.rw.food.delivery.management.repository;

import auca.ac.rw.food.delivery.management.model.Cart;
import auca.ac.rw.food.delivery.management.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    
    // To find the active cart for a customer
    Optional<Cart> findByCustomer(Customer customer);

    // Optionally, if you ever need to check if a customer has an active cart
    boolean existsByCustomer(Customer customer);
}
