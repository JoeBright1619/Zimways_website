package auca.ac.rw.food.delivery.management.repository;

import auca.ac.rw.food.delivery.management.model.DeliveryDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import auca.ac.rw.food.delivery.management.model.enums.DriverStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryDriverRepository extends JpaRepository<DeliveryDriver, UUID> {

    // ✅ Find a DeliveryDriver by name
    Optional<DeliveryDriver> findByName(String name);

    // ✅ Find DeliveryDrivers by status
    List<DeliveryDriver> findByStatus(DriverStatus status);

    // ✅ Find all DeliveryDrivers with a specific vehicle plate
    List<DeliveryDriver> findByVehiclePlate(String vehiclePlate);

    // ✅ Find DeliveryDriver by phone number
    Optional<DeliveryDriver> findByPhoneNumber(String phoneNumber);

    // ✅ Find all drivers with assigned orders
    List<DeliveryDriver> findByOrdersIsNotNull();
    
    // ✅ Count total drivers based on their status
    Long countByStatus(DriverStatus status);

    Optional<DeliveryDriver> findById(UUID id);

    void deleteById(UUID id);

    DeliveryDriver save(DeliveryDriver deliveryDriver);
}
