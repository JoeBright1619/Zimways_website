package auca.ac.rw.food.delivery.management.service;

import auca.ac.rw.food.delivery.management.model.Payment;
import auca.ac.rw.food.delivery.management.model.Order;
import auca.ac.rw.food.delivery.management.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    // Dependency Injection
    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // ✅ Create a new payment
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    // ✅ Get payment by order
    public Optional<Payment> getPaymentByOrder(Order order) {
        return paymentRepository.findByOrder(order);
    }

    // ✅ Get all payments for a specific payment method (e.g., Cash, Card)
    public List<Payment> getPaymentsByMethod(String paymentMethod) {
        return paymentRepository.findByPaymentMethod(paymentMethod);
    }

    // ✅ Get all payments above a certain amount
    public List<Payment> getPaymentsAboveAmount(double amount) {
        return paymentRepository.findByAmountGreaterThan(amount);
    }

    // ✅ Count payments by method (e.g., count how many Cash payments)
    public Long countPaymentsByMethod(String paymentMethod) {
        return paymentRepository.countByPaymentMethod(paymentMethod);
    }

    // ✅ Get payments made on a specific date
    public List<Payment> getPaymentsByDate(String paymentDate) {
        return paymentRepository.findByPaymentDate(paymentDate);
    }

    // ✅ Delete a payment by ID
    public void deletePayment(UUID paymentId) {
        paymentRepository.deleteById(paymentId);
    }
}
