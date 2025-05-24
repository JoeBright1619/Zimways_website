package auca.ac.rw.food.delivery.management.repository;

import auca.ac.rw.food.delivery.management.model.Order;
import auca.ac.rw.food.delivery.management.model.Payment;
import auca.ac.rw.food.delivery.management.model.enums.PaymentMethod;
import auca.ac.rw.food.delivery.management.model.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    // ✅ Find a payment by order
    Optional<Payment> findByOrder(Order order);

    // ✅ Get all payments by payment method (e.g., Cash, Card, Mobile Money)
    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);

    // ✅ Get payments with an amount greater than a specific value
    List<Payment> findByAmountGreaterThan(double amount);

    // ✅ Get payments by status
    List<Payment> findByStatus(PaymentStatus status);

    // ✅ Get payments made between two dates
    List<Payment> findByPaymentDateBetween(LocalDateTime start, LocalDateTime end);

    // ✅ Count payments by method (e.g., count how many were paid with cash)
    Long countByPaymentMethod(PaymentMethod paymentMethod);

    // ✅ Count payments by status
    Long countByStatus(PaymentStatus status);

    void deleteById(UUID id);

    Payment save(Payment payment);
}
