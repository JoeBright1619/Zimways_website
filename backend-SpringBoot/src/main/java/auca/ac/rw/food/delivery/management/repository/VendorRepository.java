package auca.ac.rw.food.delivery.management.repository;

import auca.ac.rw.food.delivery.management.model.Vendor;
import auca.ac.rw.food.delivery.management.model.enums.VendorStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, UUID> {

    // ✅ Find a restaurant by its name
    Optional<Vendor> findByNameIgnoreCase(String name);

    Optional<Vendor> findById(UUID id);

    void deleteById(UUID id);
    // ✅ Get restaurants located in a specific place
    List<Vendor> findByLocation(String location);

    // ✅ Get all restaurants with a specific status (e.g., "Open", "Closed")
    List<Vendor> findByStatus(VendorStatus status);

    // ✅ Find restaurants with names containing a specific keyword (case insensitive)
    List<Vendor> findByNameContainingIgnoreCase(String keyword);

    Vendor save(Vendor vendor);

    Optional<Vendor> findByNameAndLocation(String name, String location);
    // ✅ Custom delete method by name
    void deleteByName(String name);

    Optional<Vendor> findByVendorId(String vendorId);
}
