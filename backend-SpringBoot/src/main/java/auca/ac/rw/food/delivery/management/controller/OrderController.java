package auca.ac.rw.food.delivery.management.controller;

import auca.ac.rw.food.delivery.management.model.Order;
import auca.ac.rw.food.delivery.management.model.enums.OrderStatus;
import auca.ac.rw.food.delivery.management.model.enums.PaymentMethod;
import auca.ac.rw.food.delivery.management.service.OrderService;
import auca.ac.rw.food.delivery.management.DTO.OrderDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create a new order
    @PostMapping("/customer/{customerId}/driver/{driverId}")
    public ResponseEntity<Order> createOrder(
            @PathVariable UUID customerId,
            @PathVariable UUID driverId,
            @RequestBody(required = false) OrderDTO orderDTO) {
        try {
            if (orderDTO == null) {
                orderDTO = new OrderDTO();
            }
            orderDTO.setCustomerId(customerId);
            orderDTO.setDriverId(driverId);
            
            Order order = orderService.createOrder(customerId, driverId);
            
            // If delivery address is provided, update it
            if (orderDTO.getDeliveryAddress() != null && !orderDTO.getDeliveryAddress().trim().isEmpty()) {
                order.setDeliveryAddress(orderDTO.getDeliveryAddress().trim());
            }
            
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Update order status
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable UUID orderId,
            @RequestParam OrderStatus status) {
        try {
            Order order = orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Process payment for an order
    @PostMapping("/{orderId}/payment")
    public ResponseEntity<Order> processPayment(
            @PathVariable UUID orderId,
            @RequestParam PaymentMethod paymentMethod) {
        try {
            Order order = orderService.processOrderPayment(orderId, paymentMethod);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Cancel an order
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable UUID orderId,
            @RequestParam OrderStatus cancellationType) {
        try {
            if (!cancellationType.isCancellationState()) {
                return ResponseEntity.badRequest().build();
            }
            Order order = orderService.updateOrderStatus(orderId, cancellationType);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Assign driver to order
    @PutMapping("/{orderId}/driver/{driverId}")
    public ResponseEntity<Order> assignDriver(
            @PathVariable UUID orderId,
            @PathVariable UUID driverId) {
        try {
            Order order = orderService.assignDriver(orderId, driverId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Mark order as picked up
    @PutMapping("/{orderId}/picked-up")
    public ResponseEntity<Order> markOrderPickedUp(@PathVariable UUID orderId) {
        try {
            Order order = orderService.markOrderPickedUp(orderId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Mark order as out for delivery
    @PutMapping("/{orderId}/out-for-delivery")
    public ResponseEntity<Order> markOrderOutForDelivery(@PathVariable UUID orderId) {
        try {
            Order order = orderService.markOrderOutForDelivery(orderId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Mark order as delivered
    @PutMapping("/{orderId}/delivered")
    public ResponseEntity<Order> markOrderDelivered(@PathVariable UUID orderId) {
        try {
            Order order = orderService.markOrderDelivered(orderId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable UUID orderId) {
        return orderService.getOrderById(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Get orders by status
    @GetMapping("/status/{status}")
    public List<Order> getOrdersByStatus(@PathVariable OrderStatus status) {
        return orderService.getOrdersByStatus(status);
    }

    // Get orders in date range
    @GetMapping("/date-range")
    public List<Order> getOrdersInDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return orderService.getOrdersInDateRange(start, end);
    }

    // Get unassigned orders
    @GetMapping("/unassigned")
    public List<Order> getUnassignedOrders() {
        return orderService.getUnassignedOrders();
    }

    // Get order statistics
    @GetMapping("/stats/status/{status}")
    public ResponseEntity<Long> countOrdersByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.countOrdersByStatus(status));
    }
}
