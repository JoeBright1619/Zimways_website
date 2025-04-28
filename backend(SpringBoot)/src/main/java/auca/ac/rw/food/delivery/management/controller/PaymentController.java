package auca.ac.rw.food.delivery.management.controller;

import auca.ac.rw.food.delivery.management.model.Payment;
import auca.ac.rw.food.delivery.management.service.PaymentService;
import auca.ac.rw.food.delivery.management.model.Order;
import auca.ac.rw.food.delivery.management.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderRepository orderRepository;  // Add this

    // Constructor injection
    @Autowired
    public PaymentController(PaymentService paymentService, OrderRepository orderRepository) {
        this.paymentService = paymentService;
        this.orderRepository = orderRepository;  // Initialize the OrderRepository
    }

    // Create a new payment
    @PostMapping
    public Payment createPayment(@RequestBody Payment payment) {
        return paymentService.createPayment(payment);
    }

    // Get payment by order
    @GetMapping("/order/{orderId}")
    public Optional<Payment> getPaymentByOrder(@PathVariable UUID orderId) {
        // Fetch the Order using the order ID
        Optional<Order> order = orderRepository.findById(orderId);
        
        if (order.isPresent()) {
            // Pass the Order object to the service if found
            return paymentService.getPaymentByOrder(order.get());
        } else {
            // If the order is not found, return an empty Optional
            return Optional.empty();
        }
    }

    // Get all payments by payment method
    @GetMapping("/method/{paymentMethod}")
    public List<Payment> getPaymentsByMethod(@PathVariable String paymentMethod) {
        return paymentService.getPaymentsByMethod(paymentMethod);
    }

    // Get all payments greater than a specific amount
    @GetMapping("/amount/{amount}")
    public List<Payment> getPaymentsAboveAmount(@PathVariable double amount) {
        return paymentService.getPaymentsAboveAmount(amount);
    }

    // Get payment count by payment method
    @GetMapping("/count/{paymentMethod}")
    public Long countPaymentsByMethod(@PathVariable String paymentMethod) {
        return paymentService.countPaymentsByMethod(paymentMethod);
    }

    // Get payments by date
    @GetMapping("/date/{paymentDate}")
    public List<Payment> getPaymentsByDate(@PathVariable String paymentDate) {
        return paymentService.getPaymentsByDate(paymentDate);
    }

    // Delete a payment by ID
    @DeleteMapping("/{paymentId}")
    public void deletePayment(@PathVariable UUID paymentId) {
        paymentService.deletePayment(paymentId);
    }
}
