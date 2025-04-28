package auca.ac.rw.food.delivery.management.repository;

import auca.ac.rw.food.delivery.management.model.Customer;
import auca.ac.rw.food.delivery.management.model.DeliveryDriver;
import auca.ac.rw.food.delivery.management.model.Order;
import auca.ac.rw.food.delivery.management.model.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    // ✅ Find orders by status
    List<Order> findByStatus(OrderStatus status);

    // ✅ Find all orders for a specific customer
    List<Order> findByCustomer(Customer customer);

    // ✅ Find orders assigned to a specific delivery driver
    List<Order> findByDeliveryDriver(DeliveryDriver driver);

    // ✅ Find all orders placed within a specific date range
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // ✅ Find all completed orders for a customer
    List<Order> findByCustomerAndStatus(Customer customer, OrderStatus status);

    // ✅ Find orders that haven't been assigned a driver yet
    List<Order> findByDeliveryDriverIsNull();

    // ✅ Count total orders for a specific status
    Long countByStatus(OrderStatus status);

    // ✅ Optional: Find the latest order by a customer
    Optional<Order> findTopByCustomerOrderByOrderDateDesc(Customer customer);

    // ✅ Custom delete method — cancel all orders by a customer
    void deleteByCustomer(Customer customer);

    // ✅ Find orders with total price greater than a specific amount
    List<Order> findByTotalGreaterThan(Double amount);

    // ✅ Find orders that have a payment linked
    //List<Order> findByPaymentIsNotNull();

    Order save(Order order);

    Optional<Order> findById(UUID id);

    void deleteById(UUID id);
}
