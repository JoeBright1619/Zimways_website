package auca.ac.rw.food.delivery.management.repository;

import auca.ac.rw.food.delivery.management.model.Order;
import auca.ac.rw.food.delivery.management.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    // ✅ Find a payment by order
    Optional<Payment> findByOrder(Order order);

    // ✅ Get all payments by payment method (e.g., Cash, Card, Mobile Money)
    List<Payment> findByPaymentMethod(String paymentMethod);

    // ✅ Get payments with an amount greater than a specific value
    List<Payment> findByAmountGreaterThan(double amount);

    // ✅ Count payments by method (e.g., count how many were paid with cash)
    Long countByPaymentMethod(String paymentMethod);

    // ✅ Find payments made on a specific date
    List<Payment> findByPaymentDate(String paymentDate);

    void deleteById(UUID id);

    Payment save(Payment payment);
}
