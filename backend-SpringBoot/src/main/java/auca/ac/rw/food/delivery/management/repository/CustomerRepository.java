package auca.ac.rw.food.delivery.management.repository;

import auca.ac.rw.food.delivery.management.model.Cart;
import auca.ac.rw.food.delivery.management.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
   
    Customer save(Customer customer);
     // ✅ Find a customer by ID (Explicitly added for clarity)
     Optional<Customer> findById(UUID id);

     Optional<Customer> findByEmailAndPassword(String email, String password);
     // ✅ Delete a cart by ID (Explicitly added for clarity)
     void deleteById(UUID id);

     // Add findByEmail method
     Optional<Customer> findByEmail(String email);
}
