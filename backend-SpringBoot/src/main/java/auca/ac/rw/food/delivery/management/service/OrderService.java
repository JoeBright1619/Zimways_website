package auca.ac.rw.food.delivery.management.service;

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
    // ✅ Constructor injection (best practice)
    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    // 🎯 Create a new order
    public Order createOrder(Order order) {
        Optional<Customer> cust = customerRepository.findById(order.getCustomer().getId());

        if (!cust.isPresent()) {
            throw new IllegalArgumentException("Customer not found.");
        }
        order.setCart(cust.get().getCart());
        order.setTotal(order.getCart().getAmount());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
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
