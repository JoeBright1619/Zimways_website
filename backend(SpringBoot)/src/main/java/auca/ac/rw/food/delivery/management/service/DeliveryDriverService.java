package auca.ac.rw.food.delivery.management.service;

import auca.ac.rw.food.delivery.management.model.DeliveryDriver;
import auca.ac.rw.food.delivery.management.repository.DeliveryDriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeliveryDriverService {

    @Autowired
    private DeliveryDriverRepository deliveryDriverRepository;

    // Get a delivery driver by ID
    public Optional<DeliveryDriver> getDriverById(UUID driverId) {
        return deliveryDriverRepository.findById(driverId);
    }

    // Get a delivery driver by name
    public Optional<DeliveryDriver> getDriverByName(String name) {
        return deliveryDriverRepository.findByName(name);
    }

    // Get all delivery drivers with a specific status
    public List<DeliveryDriver> getDriversByStatus(String status) {
        return deliveryDriverRepository.findByStatus(status);
    }

    // Get all delivery drivers with a specific vehicle plate
    public List<DeliveryDriver> getDriversByVehiclePlate(String vehiclePlate) {
        return deliveryDriverRepository.findByVehiclePlate(vehiclePlate);
    }

    // Get a driver by phone number
    public Optional<DeliveryDriver> getDriverByPhoneNumber(String phoneNumber) {
        return deliveryDriverRepository.findByPhoneNumber(phoneNumber);
    }

    // Get all drivers assigned to orders
    public List<DeliveryDriver> getDriversWithOrders() {
        return deliveryDriverRepository.findByOrdersIsNotNull();
    }

    // Count the total number of drivers with a specific status
    public Long countDriversByStatus(String status) {
        return deliveryDriverRepository.countByStatus(status);
    }

    // Save a new delivery driver or update an existing one
    public DeliveryDriver saveDriver(DeliveryDriver driver) {
        return deliveryDriverRepository.save(driver);
    }

    // Delete a delivery driver by ID
    public void deleteDriver(UUID driverId) {
        deliveryDriverRepository.deleteById(driverId);
    }
}
