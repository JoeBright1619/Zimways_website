package auca.ac.rw.food.delivery.management.controller;

import auca.ac.rw.food.delivery.management.model.Payment;
import auca.ac.rw.food.delivery.management.model.enums.PaymentMethod;
import auca.ac.rw.food.delivery.management.model.enums.PaymentStatus;
import auca.ac.rw.food.delivery.management.service.PaymentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Create a new payment for an order
    @PostMapping("/order/{orderId}")
    public ResponseEntity<Payment> createPayment(
            @PathVariable UUID orderId,
            @RequestParam PaymentMethod paymentMethod) {
        try {
            Payment payment = paymentService.createPayment(orderId, paymentMethod);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Process a payment
    @PostMapping("/{paymentId}/process")
    public ResponseEntity<Payment> processPayment(@PathVariable UUID paymentId) {
        try {
            Payment payment = paymentService.processPayment(paymentId);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get payment by ID
    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable UUID paymentId) {
        return paymentService.getPaymentById(paymentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all payments
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    // Get payments by status
    @GetMapping("/status/{status}")
    public List<Payment> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        return paymentService.getPaymentsByStatus(status);
    }

    // Get payments by method
    @GetMapping("/method/{paymentMethod}")
    public List<Payment> getPaymentsByMethod(@PathVariable PaymentMethod paymentMethod) {
        return paymentService.getPaymentsByMethod(paymentMethod);
    }

    // Get payments in date range
    @GetMapping("/date-range")
    public List<Payment> getPaymentsInDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return paymentService.getPaymentsInDateRange(start, end);
    }

    // Get payments above amount
    @GetMapping("/amount/{amount}")
    public List<Payment> getPaymentsAboveAmount(@PathVariable double amount) {
        return paymentService.getPaymentsAboveAmount(amount);
    }

    // Refund a payment
    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<Payment> refundPayment(@PathVariable UUID paymentId) {
        try {
            Payment payment = paymentService.refundPayment(paymentId);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Cancel a payment
    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<Payment> cancelPayment(@PathVariable UUID paymentId) {
        try {
            Payment payment = paymentService.cancelPayment(paymentId);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get payment statistics
    @GetMapping("/stats/status/{status}")
    public ResponseEntity<Long> countPaymentsByStatus(@PathVariable PaymentStatus status) {
        return ResponseEntity.ok(paymentService.countPaymentsByStatus(status));
    }

    @GetMapping("/stats/method/{paymentMethod}")
    public ResponseEntity<Long> countPaymentsByMethod(@PathVariable PaymentMethod paymentMethod) {
        return ResponseEntity.ok(paymentService.countPaymentsByMethod(paymentMethod));
    }

    // Delete a payment (only for failed/cancelled payments)
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deletePayment(@PathVariable UUID paymentId) {
        try {
            paymentService.deletePayment(paymentId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
