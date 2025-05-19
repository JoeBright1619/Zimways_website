package auca.ac.rw.food.delivery.management.repository;

import auca.ac.rw.food.delivery.management.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    // You can add custom query methods here if needed, e.g.,
    // List<CartItem> findByCartId(UUID cartId);
}
