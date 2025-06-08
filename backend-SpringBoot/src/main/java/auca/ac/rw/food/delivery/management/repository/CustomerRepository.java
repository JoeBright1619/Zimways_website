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

     // ✅ Find by email and password (though we might want to remove this for Firebase auth)
     Optional<Customer> findByEmailAndPassword(String email, String password);
     // ✅ Delete a cart by ID (Explicitly added for clarity)
     void deleteById(UUID id);

     // ✅ Find by email - this is what we'll use for Firebase auth
     Optional<Customer> findByEmail(String email);

          // Add findByEmail method
    //Optional<Customer> findByEmailAndFirebaseToken(String email, String firebaseToken);
     // ✅ Find by Firebase UID
     Optional<Customer> findByFirebaseUid(String firebaseUid);
}
