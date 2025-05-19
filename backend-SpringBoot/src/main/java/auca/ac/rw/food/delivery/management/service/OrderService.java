package auca.ac.rw.food.delivery.management.service;

import auca.ac.rw.food.delivery.management.model.Cart;
import auca.ac.rw.food.delivery.management.model.CartItem;
import auca.ac.rw.food.delivery.management.model.Customer;
import auca.ac.rw.food.delivery.management.model.DeliveryDriver;
import auca.ac.rw.food.delivery.management.model.Order;
import auca.ac.rw.food.delivery.management.model.enums.OrderStatus;
import auca.ac.rw.food.delivery.management.repository.*;
import org.springframework.stereotype.Service;

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

    // ✅ Constructor injection (best practice)
    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository,
                        CartRepository cartRepository, DeliveryDriverRepository driverRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.driverRepository = driverRepository;
    }

    // 🎯 Create a new order
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

    double total = 0.0;
    for (CartItem cartItem : cart.getCartItems()) {
        total += cartItem.getItem().getPrice() * cartItem.getQuantity();
    }

    double deliveryFee = 200.0;
    total += deliveryFee;

    Order order = new Order();
    order.setCustomer(customer);
    order.setCart(cart);
    order.setStatus(OrderStatus.PENDING);
    order.setOrderDate(LocalDateTime.now());
    order.setDeliveryDriver(driver);
    order.setTotal(total);

    // optionally: clear cart or delete it
    cartRepository.delete(cart);

    return orderRepository.save(order);
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

    // 🎯 Cancel all orders for a customer
    public void cancelOrdersByCustomer(Customer customer) {
        orderRepository.deleteByCustomer(customer);
    }

    // 🎯 Get orders with total price greater than a specific amount
    public List<Order> getOrdersWithTotalGreaterThan(Double amount) {
        return orderRepository.findByTotalGreaterThan(amount);
    }

    // 🎯 Get orders that have been paid for
    /*public List<Order> getOrdersWithPayment() {
        return orderRepository.findByPaymentIsNotNull();
    }*/

    // 🎯 Update order status
    public Order updateOrderStatus(UUID id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public Order updateOrder(UUID id, Order updatedOrder) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setStatus(updatedOrder.getStatus());
                    existingOrder.setReceivedDate(updatedOrder.getReceivedDate());
                    return orderRepository.save(existingOrder);
                }).orElseThrow(() -> new RuntimeException("Order not found"));
    }


    // 🎯 Delete an order by ID
    public void deleteOrder(UUID id) {
        orderRepository.deleteById(id);
    }
}
