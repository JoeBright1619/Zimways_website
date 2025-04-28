package auca.ac.rw.food.delivery.management.controller;

import auca.ac.rw.food.delivery.management.model.Restaurant;
import auca.ac.rw.food.delivery.management.model.enums.RestaurantStatus;
import auca.ac.rw.food.delivery.management.service.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    // ✅ Get all restaurants
    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }

    // ✅ Get a restaurant by ID
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable UUID id) {
        return restaurantService.getRestaurantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Get a restaurant by name
    @GetMapping("/name/{name}")
    public ResponseEntity<Restaurant> getRestaurantByName(@PathVariable String name) {
        return restaurantService.getRestaurantByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Create a new restaurant
    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);
        return ResponseEntity.ok(createdRestaurant);
    }

    // ✅ Update restaurant details
    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable UUID id, @RequestBody Restaurant updatedRestaurant) {
        try {
            Restaurant restaurant = restaurantService.updateRestaurant(id, updatedRestaurant);
            return ResponseEntity.ok(restaurant);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Delete a restaurant by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable UUID id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Delete a restaurant by name
    @DeleteMapping("/name/{name}")
    public ResponseEntity<Void> deleteRestaurantByName(@PathVariable String name) {
        restaurantService.deleteRestaurantByName(name);
        return ResponseEntity.noContent().build();
    }

    // ✅ Get restaurants by location
    @GetMapping("/location/{location}")
    public ResponseEntity<List<Restaurant>> getRestaurantsByLocation(@PathVariable String location) {
        List<Restaurant> restaurants = restaurantService.getRestaurantsByLocation(location);
        return ResponseEntity.ok(restaurants);
    }

    // ✅ Get restaurants by status (Open/Closed)
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Restaurant>> getRestaurantsByStatus(@PathVariable RestaurantStatus status) {
        List<Restaurant> restaurants = restaurantService.getRestaurantsByStatus(status);
        return ResponseEntity.ok(restaurants);
    }

    // ✅ Search restaurants by keyword in name
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Restaurant>> searchRestaurantsByName(@PathVariable String keyword) {
        List<Restaurant> restaurants = restaurantService.searchRestaurantsByName(keyword);
        return ResponseEntity.ok(restaurants);
    }
}
