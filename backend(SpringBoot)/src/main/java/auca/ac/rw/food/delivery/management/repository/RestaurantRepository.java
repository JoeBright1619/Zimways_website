package auca.ac.rw.food.delivery.management.repository;

import auca.ac.rw.food.delivery.management.model.Restaurant;
import auca.ac.rw.food.delivery.management.model.enums.RestaurantStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    // ✅ Find a restaurant by its name
    Optional<Restaurant> findByName(String name);

    Optional<Restaurant> findById(UUID id);

    void deleteById(UUID id);
    // ✅ Get restaurants located in a specific place
    List<Restaurant> findByLocation(String location);

    // ✅ Get all restaurants with a specific status (e.g., "Open", "Closed")
    List<Restaurant> findByStatus(RestaurantStatus status);

    // ✅ Find restaurants with names containing a specific keyword (case insensitive)
    List<Restaurant> findByNameContainingIgnoreCase(String keyword);

    Restaurant save(Restaurant restaurant);

    Optional<Restaurant> findByNameAndLocation(String name, String location);
    // ✅ Custom delete method by name
    void deleteByName(String name);
}
