package auca.ac.rw.food.delivery.management.service;

import auca.ac.rw.food.delivery.management.event.OrderStatusChangeEvent;
import auca.ac.rw.food.delivery.management.model.Cart;
import auca.ac.rw.food.delivery.management.model.CartItem;
import auca.ac.rw.food.delivery.management.model.Customer;
import auca.ac.rw.food.delivery.management.model.DeliveryDriver;
import auca.ac.rw.food.delivery.management.model.Order;
import auca.ac.rw.food.delivery.management.model.enums.OrderStatus;
import auca.ac.rw.food.delivery.management.model.enums.PaymentMethod;
import auca.ac.rw.food.delivery.management.repository.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import auca.ac.rw.food.delivery.management.service.CartService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final DeliveryDriverRepository driverRepository;
    private final CartService cartService;
    private final ApplicationEventPublisher eventPublisher;

    // ✅ Constructor injection (best practice)
    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository,
                        CartRepository cartRepository, DeliveryDriverRepository driverRepository,
                        CartService cartService,
                        ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.driverRepository = driverRepository;
        this.cartService = cartService;
        this.eventPublisher = eventPublisher;
    }

    // 🎯 Create a new order
    @Transactional
    public Order createOrder(UUID customerId, UUID driverId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

        DeliveryDriver driver = driverRepository.findById(driverId)
            .orElseThrow(() -> new RuntimeException("Driver not found"));
        
        Cart cart = cartRepository.findByCustomer(customer)
            .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double total = calculateOrderTotal(cart);
        double deliveryFee = 200.0;
        total += deliveryFee;

        Order order = new Order();
        order.setCustomer(customer);
        order.setCart(cart);
        order.setStatus(OrderStatus.PENDING); // Start with PENDING status
        order.setOrderDate(LocalDateTime.now());
        order.setDeliveryDriver(driver);
        order.setTotal(total);

        // Clear the cart after order creation
        cartService.clearCart(cart);

        return orderRepository.save(order);
    }

    // 🎯 Update order status with validation
    @Transactional
    public Order updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus oldStatus = order.getStatus();

        // Validate status transition
        if (!oldStatus.canTransitionTo(newStatus)) {
            throw new RuntimeException("Invalid status transition from " + 
                oldStatus + " to " + newStatus);
        }

        // Handle specific status transitions
        switch (newStatus) {
            case CONFIRMED:
                // Restaurant has confirmed the order
                break;
            case PREPARING:
                // Restaurant has started preparing
                break;
            case PAYMENT_PENDING:
                // Payment is now required
                break;
            case PAYMENT_COMPLETED:
                // Payment successful, can proceed to delivery
                break;
            case READY_FOR_PICKUP:
                // Food is ready for driver pickup
                break;
            case DRIVER_ASSIGNED:
                // Driver has been assigned
                break;
            case DRIVER_PICKED_UP:
                // Driver has picked up the order
                break;
            case OUT_FOR_DELIVERY:
                // Driver is delivering
                break;
            case DELIVERED:
                // Order has been delivered
                order.setReceivedDate(LocalDateTime.now());
                break;
            case COMPLETED:
                // Order is fully completed
                break;
            case CANCELLED_BY_CUSTOMER:
            case CANCELLED_BY_RESTAURANT:
            case CANCELLED_BY_SYSTEM:
                // Handle cancellation
                handleOrderCancellation(order, newStatus);
                break;
            case REFUNDED:
                // Handle refund
                handleOrderRefund(order);
                break;
        }

        order.setStatus(newStatus);
        order = orderRepository.save(order);
        
        // Publish event for status change
        eventPublisher.publishEvent(new OrderStatusChangeEvent(this, order, oldStatus, newStatus));
        
        return order;
    }

    // 🎯 Cancel an order
    @Transactional
    public Order cancelOrder(UUID orderId, OrderStatus cancellationType) {
        if (!cancellationType.isCancellationState()) {
            throw new RuntimeException("Invalid cancellation status: " + cancellationType);
        }
        return updateOrderStatus(orderId, cancellationType);
    }

    // 🎯 Process payment for an order
    @Transactional
    public Order processOrderPayment(UUID orderId, PaymentMethod paymentMethod) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getStatus().isPaymentState() && order.getStatus() != OrderStatus.PREPARING) {
            throw new RuntimeException("Order is not in a payment state");
        }

        // Update order status to payment processing
        return updateOrderStatus(orderId, OrderStatus.PAYMENT_PROCESSING);
    }

    // 🎯 Assign driver to order
    @Transactional
    public Order assignDriver(UUID orderId, UUID driverId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.READY_FOR_PICKUP) {
            throw new RuntimeException("Order is not ready for driver assignment");
        }

        DeliveryDriver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        order.setDeliveryDriver(driver);
        return updateOrderStatus(orderId, OrderStatus.DRIVER_ASSIGNED);
    }

    // 🎯 Mark order as picked up by driver
    @Transactional
    public Order markOrderPickedUp(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.DRIVER_ASSIGNED) {
            throw new RuntimeException("Order is not assigned to a driver");
        }

        return updateOrderStatus(orderId, OrderStatus.DRIVER_PICKED_UP);
    }

    // 🎯 Mark order as out for delivery
    @Transactional
    public Order markOrderOutForDelivery(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.DRIVER_PICKED_UP) {
            throw new RuntimeException("Order has not been picked up by driver");
        }

        return updateOrderStatus(orderId, OrderStatus.OUT_FOR_DELIVERY);
    }

    // 🎯 Mark order as delivered
    @Transactional
    public Order markOrderDelivered(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.OUT_FOR_DELIVERY) {
            throw new RuntimeException("Order is not out for delivery");
        }

        order = updateOrderStatus(orderId, OrderStatus.DELIVERED);
        return updateOrderStatus(orderId, OrderStatus.COMPLETED);
    }

    // Helper methods
    private double calculateOrderTotal(Cart cart) {
        return cart.getCartItems().stream()
                .mapToDouble(item -> item.getItem().getPrice() * item.getQuantity())
                .sum();
    }

    private void handleOrderCancellation(Order order, OrderStatus cancellationType) {
        // Handle any cleanup needed for cancellation
        // For example, notify customer, restaurant, or driver
        // Release driver if assigned
        if (order.getDeliveryDriver() != null) {
            order.setDeliveryDriver(null);
        }
    }

    private void handleOrderRefund(Order order) {
        // Handle refund process
        // This might involve calling payment service to process refund
        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new RuntimeException("Only completed orders can be refunded");
        }
    }

    // 🎯 Get order by ID
    public Optional<Order> getOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    // 🎯 Get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // 🎯 Get orders by status
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    // 🎯 Get all orders for a specific customer
    public List<Order> getOrdersByCustomer(Customer customer) {
        return orderRepository.findByCustomer(customer);
    }

    // 🎯 Get orders assigned to a specific driver
    public List<Order> getOrdersByDriver(DeliveryDriver driver) {
        return orderRepository.findByDeliveryDriver(driver);
    }

    // 🎯 Get orders in a specific date range
    public List<Order> getOrdersInDateRange(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findByOrderDateBetween(start, end);
    }

    // 🎯 Get completed orders for a customer
    public List<Order> getCompletedOrdersByCustomer(Customer customer) {
        return orderRepository.findByCustomerAndStatus(customer, OrderStatus.COMPLETED);
    }

    // 🎯 Get unassigned orders
    public List<Order> getUnassignedOrders() {
        return orderRepository.findByDeliveryDriverIsNull();
    }

    // 🎯 Count total orders by status
    public Long countOrdersByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    // 🎯 Get latest order for a customer
    public Optional<Order> getLatestOrderByCustomer(Customer customer) {
        return orderRepository.findTopByCustomerOrderByOrderDateDesc(customer);
    }

    // 🎯 Get orders with total price greater than a specific amount
    public List<Order> getOrdersWithTotalGreaterThan(Double amount) {
        return orderRepository.findByTotalGreaterThan(amount);
    }

    // 🎯 Delete an order by ID
    public void deleteOrder(UUID id) {
        orderRepository.deleteById(id);
    }
}
