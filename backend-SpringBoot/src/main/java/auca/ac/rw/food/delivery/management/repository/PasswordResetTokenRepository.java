package auca.ac.rw.food.delivery.management.repository;

import auca.ac.rw.food.delivery.management.model.PasswordResetToken;
import auca.ac.rw.food.delivery.management.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByCustomerAndUsedFalse(Customer customer);
} 