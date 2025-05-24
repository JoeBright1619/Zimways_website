package auca.ac.rw.food.delivery.management.service;

import auca.ac.rw.food.delivery.management.event.OrderStatusChangeEvent;
import auca.ac.rw.food.delivery.management.model.Payment;
import auca.ac.rw.food.delivery.management.model.Order;
import auca.ac.rw.food.delivery.management.model.enums.PaymentMethod;
import auca.ac.rw.food.delivery.management.model.enums.PaymentStatus;
import auca.ac.rw.food.delivery.management.model.enums.OrderStatus;
import auca.ac.rw.food.delivery.management.repository.PaymentRepository;
import auca.ac.rw.food.delivery.management.repository.OrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PaymentService(PaymentRepository paymentRepository, 
                         OrderRepository orderRepository,
                         ApplicationEventPublisher eventPublisher) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @EventListener
    @Transactional
    public void handleOrderStatusChange(OrderStatusChangeEvent event) {
        Order order = event.getOrder();
        OrderStatus newStatus = event.getNewStatus();
        OrderStatus oldStatus = event.getOldStatus();

        // Handle payment-related status changes
        switch (newStatus) {
            case PAYMENT_PENDING:
                // Payment is now required
                break;
            case PAYMENT_PROCESSING:
                // Payment is being processed
                break;
            case PAYMENT_COMPLETED:
                // Payment was successful
                Optional<Payment> paymentOpt = paymentRepository.findByOrder(order);
                if (paymentOpt.isPresent()) {
                    Payment payment = paymentOpt.get();
                    payment.setStatus(PaymentStatus.COMPLETED);
                    paymentRepository.save(payment);
                }
                break;
            case PAYMENT_FAILED:
                // Payment failed
                paymentOpt = paymentRepository.findByOrder(order);
                if (paymentOpt.isPresent()) {
                    Payment payment = paymentOpt.get();
                    payment.setStatus(PaymentStatus.FAILED);
                    paymentRepository.save(payment);
                }
                break;
            case REFUNDED:
                // Handle refund
                paymentOpt = paymentRepository.findByOrder(order);
                if (paymentOpt.isPresent()) {
                    Payment payment = paymentOpt.get();
                    payment.setStatus(PaymentStatus.REFUNDED);
                    paymentRepository.save(payment);
                }
                break;
        }
    }

    // ✅ Create a new payment for an order
    @Transactional
    public Payment createPayment(UUID orderId, PaymentMethod paymentMethod) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        // Check if payment already exists for this order
        if (paymentRepository.findByOrder(order).isPresent()) {
            throw new RuntimeException("Payment already exists for this order");
        }

        // Validate order status
        if (!order.getStatus().isPaymentState() && order.getStatus() != OrderStatus.PREPARING) {
            throw new RuntimeException("Order is not in a valid state for payment");
        }

        Payment payment = new Payment(order.getTotal(), paymentMethod, order);
        payment = paymentRepository.save(payment);

        // Publish event for payment creation
        eventPublisher.publishEvent(new OrderStatusChangeEvent(this, order, order.getStatus(), OrderStatus.PAYMENT_PENDING));

        return payment;
    }

    // ✅ Process a payment
    @Transactional
    public Payment processPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Payment is not in PENDING status");
        }

        Order order = payment.getOrder();

        // Update payment status to processing
        payment.setStatus(PaymentStatus.PROCESSING);
        payment = paymentRepository.save(payment);

        // Publish event for payment processing
        eventPublisher.publishEvent(new OrderStatusChangeEvent(this, order, order.getStatus(), OrderStatus.PAYMENT_PROCESSING));

        try {
            // TODO: Integrate with actual payment gateway
            Thread.sleep(3000); // Simulate processing time
            
            // Update payment status to completed
            payment.setStatus(PaymentStatus.COMPLETED);
            payment = paymentRepository.save(payment);

            // Publish event for payment completion
            eventPublisher.publishEvent(new OrderStatusChangeEvent(this, order, order.getStatus(), OrderStatus.PAYMENT_COMPLETED));

            // If order was in preparing state, move it to ready for pickup
            if (order.getStatus() == OrderStatus.PAYMENT_COMPLETED) {
                eventPublisher.publishEvent(new OrderStatusChangeEvent(this, order, order.getStatus(), OrderStatus.READY_FOR_PICKUP));
            }
        } catch (Exception e) {
            // Update payment status to failed
            payment.setStatus(PaymentStatus.FAILED);
            payment = paymentRepository.save(payment);

            // Publish event for payment failure
            eventPublisher.publishEvent(new OrderStatusChangeEvent(this, order, order.getStatus(), OrderStatus.PAYMENT_FAILED));
        }

        return payment;
    }

    // ✅ Get payment by ID
    public Optional<Payment> getPaymentById(UUID id) {
        return paymentRepository.findById(id);
    }

    // ✅ Get payment by order
    public Optional<Payment> getPaymentByOrder(Order order) {
        return paymentRepository.findByOrder(order);
    }

    // ✅ Get all payments
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // ✅ Get payments by status
    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }

    // ✅ Get payments by method
    public List<Payment> getPaymentsByMethod(PaymentMethod paymentMethod) {
        return paymentRepository.findByPaymentMethod(paymentMethod);
    }

    // ✅ Get payments in date range
    public List<Payment> getPaymentsInDateRange(LocalDateTime start, LocalDateTime end) {
        return paymentRepository.findByPaymentDateBetween(start, end);
    }

    // ✅ Get payments above amount
    public List<Payment> getPaymentsAboveAmount(double amount) {
        return paymentRepository.findByAmountGreaterThan(amount);
    }

    // ✅ Refund a payment
    @Transactional
    public Payment refundPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new RuntimeException("Only completed payments can be refunded");
        }

        Order order = payment.getOrder();

        // Update payment status to refunded
        payment.setStatus(PaymentStatus.REFUNDED);
        payment = paymentRepository.save(payment);

        // Publish event for refund
        eventPublisher.publishEvent(new OrderStatusChangeEvent(this, order, order.getStatus(), OrderStatus.REFUNDED));

        return payment;
    }

    // ✅ Cancel a payment
    @Transactional
    public Payment cancelPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Only pending payments can be cancelled");
        }

        Order order = payment.getOrder();

        // Update payment status to cancelled
        payment.setStatus(PaymentStatus.CANCELLED);
        payment = paymentRepository.save(payment);

        // Publish appropriate event based on order status
        if (order.getStatus() == OrderStatus.PAYMENT_PENDING) {
            // If payment was pending, move back to preparing
            eventPublisher.publishEvent(new OrderStatusChangeEvent(this, order, order.getStatus(), OrderStatus.PREPARING));
        } else {
            // Otherwise, mark as cancelled by system
            eventPublisher.publishEvent(new OrderStatusChangeEvent(this, order, order.getStatus(), OrderStatus.CANCELLED_BY_SYSTEM));
        }

        return payment;
    }

    // ✅ Get payment statistics
    public Long countPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.countByStatus(status);
    }

    public Long countPaymentsByMethod(PaymentMethod paymentMethod) {
        return paymentRepository.countByPaymentMethod(paymentMethod);
    }

    // ✅ Delete a payment (only for failed/cancelled payments)
    @Transactional
    public void deletePayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.FAILED && 
            payment.getStatus() != PaymentStatus.CANCELLED) {
            throw new RuntimeException("Only failed or cancelled payments can be deleted");
        }

        paymentRepository.delete(payment);
    }
}
