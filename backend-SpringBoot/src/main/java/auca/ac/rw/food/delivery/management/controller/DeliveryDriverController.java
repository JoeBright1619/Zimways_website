package auca.ac.rw.food.delivery.management.controller;

import auca.ac.rw.food.delivery.management.model.DeliveryDriver;
import auca.ac.rw.food.delivery.management.service.DeliveryDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/delivery-drivers")
public class DeliveryDriverController {

    @Autowired
    private DeliveryDriverService deliveryDriverService;

    // Get a delivery driver by ID
    @GetMapping("/{driverId}")
    public ResponseEntity<DeliveryDriver> getDriverById(@PathVariable UUID driverId) {
        Optional<DeliveryDriver> driver = deliveryDriverService.getDriverById(driverId);
        return driver.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Get a delivery driver by name
    @GetMapping("/name/{name}")
    public ResponseEntity<DeliveryDriver> getDriverByName(@PathVariable String name) {
        Optional<DeliveryDriver> driver = deliveryDriverService.getDriverByName(name);
        return driver.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Get all delivery drivers with a specific status
    @GetMapping("/status/{status}")
    public List<DeliveryDriver> getDriversByStatus(@PathVariable String status) {
        return deliveryDriverService.getDriversByStatus(status);
    }

    // Get all delivery drivers with a specific vehicle plate
    @GetMapping("/vehicle/{vehiclePlate}")
    public List<DeliveryDriver> getDriversByVehiclePlate(@PathVariable String vehiclePlate) {
        return deliveryDriverService.getDriversByVehiclePlate(vehiclePlate);
    }

    // Get a driver by phone number
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<DeliveryDriver> getDriverByPhoneNumber(@PathVariable String phoneNumber) {
        Optional<DeliveryDriver> driver = deliveryDriverService.getDriverByPhoneNumber(phoneNumber);
        return driver.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Get all drivers with orders assigned
    @GetMapping("/with-orders")
    public List<DeliveryDriver> getDriversWithOrders() {
        return deliveryDriverService.getDriversWithOrders();
    }

    // Count the total number of drivers with a specific status
    @GetMapping("/count/status/{status}")
    public Long countDriversByStatus(@PathVariable String status) {
        return deliveryDriverService.countDriversByStatus(status);
    }

    // Create or update a delivery driver
    @PostMapping
    public ResponseEntity<DeliveryDriver> saveDriver(@RequestBody DeliveryDriver driver) {
        DeliveryDriver savedDriver = deliveryDriverService.saveDriver(driver);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDriver);
    }

    // Delete a delivery driver by ID
    @DeleteMapping("/{driverId}")
    public ResponseEntity<Void> deleteDriver(@PathVariable UUID driverId) {
        deliveryDriverService.deleteDriver(driverId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
