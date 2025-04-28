package auca.ac.rw.food.delivery.management.service;

import auca.ac.rw.food.delivery.management.model.Restaurant;
import auca.ac.rw.food.delivery.management.model.enums.RestaurantStatus;
import auca.ac.rw.food.delivery.management.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    // ✅ Get all restaurants
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    // ✅ Get a restaurant by ID
    public Optional<Restaurant> getRestaurantById(UUID id) {
        return restaurantRepository.findById(id);
    }

    // ✅ Get a restaurant by name
    public Optional<Restaurant> getRestaurantByName(String name) {
        return restaurantRepository.findByName(name);
    }

    // ✅ Create a new restaurant
    public Restaurant createRestaurant(Restaurant restaurant) {
         // Check if restaurant with the same name and address exists
    Optional<Restaurant> existingRestaurant = restaurantRepository
    .findByNameAndLocation(restaurant.getName(), restaurant.getLocation());

    if (existingRestaurant.isPresent()) {
    throw new IllegalStateException("Restaurant with the same name and address already exists.");
    }

    // Proceed to save if no duplicate
    return restaurantRepository.save(restaurant);
    }

    // ✅ Update restaurant details
    public Restaurant updateRestaurant(UUID id, Restaurant updatedRestaurant) {
        return restaurantRepository.findById(id)
                .map(existingRestaurant -> {
                    existingRestaurant.setName(updatedRestaurant.getName());
                    existingRestaurant.setLocation(updatedRestaurant.getLocation());
                    existingRestaurant.setPhone(updatedRestaurant.getPhone());
                    existingRestaurant.setStatus(updatedRestaurant.getStatus());
                    return restaurantRepository.save(existingRestaurant);
                })
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    // ✅ Delete a restaurant by ID
    public void deleteRestaurant(UUID id) {
        restaurantRepository.deleteById(id);
    }

    // ✅ Delete a restaurant by name (custom)
    public void deleteRestaurantByName(String name) {
        restaurantRepository.deleteByName(name);
    }

    // ✅ Get restaurants by location
    public List<Restaurant> getRestaurantsByLocation(String location) {
        return restaurantRepository.findByLocation(location);
    }

    // ✅ Get restaurants by status (Open/Closed)
    public List<Restaurant> getRestaurantsByStatus(RestaurantStatus status) {
        return restaurantRepository.findByStatus(status);
    }

    // ✅ Search restaurants by keyword in name
    public List<Restaurant> searchRestaurantsByName(String keyword) {
        return restaurantRepository.findByNameContainingIgnoreCase(keyword);
    }
}
