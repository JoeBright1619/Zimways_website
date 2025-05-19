package auca.ac.rw.food.delivery.management.service;

import auca.ac.rw.food.delivery.management.model.Vendor;
import auca.ac.rw.food.delivery.management.model.enums.VendorStatus;
import auca.ac.rw.food.delivery.management.repository.VendorRepository;
import auca.ac.rw.food.delivery.management.DTO.VendorDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.management.relation.RelationNotFoundException;

@Service
public class VendorService {
    private final VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    // ✅ Get all vendors
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    // ✅ Get a vendor by ID
    public Optional<Vendor> getVendorById(UUID id) {
        return vendorRepository.findById(id);
    }

    // ✅ Get a vendor by name
    public Optional<Vendor> getVendorByName(String name) {
        return vendorRepository.findByNameIgnoreCase(name);
    }

    // ✅ Create a new vendor
    public Vendor createVendor(Vendor vendor) {
        // Check if vendor with the same name and address exists
        Optional<Vendor> existingVendor = vendorRepository
                .findByNameAndLocation(vendor.getName(), vendor.getLocation());

        if (existingVendor.isPresent()) {
            throw new IllegalStateException("Vendor with the same name and address already exists.");
        }

        // Proceed to save if no duplicate
        return vendorRepository.save(vendor);
    }

    // ✅ Update vendor details
public Vendor updateVendor(UUID id, VendorDTO updatedVendor) {
    return vendorRepository.findById(id)
            .map(existingVendor -> {
                if (updatedVendor.getName() != null) {
                    existingVendor.setName(updatedVendor.getName());
                }
                if (updatedVendor.getLocation() != null) {
                    existingVendor.setLocation(updatedVendor.getLocation());
                }
                if (updatedVendor.getPhone() != null) {
                    existingVendor.setPhone(updatedVendor.getPhone());
                }
                if (updatedVendor.getEmail() != null) {
                    existingVendor.setEmail(updatedVendor.getEmail());
                }
                if (updatedVendor.getVendorType() != null) {
                    existingVendor.setVendorType(updatedVendor.getVendorType());
                }
                if (updatedVendor.getDescription() != null) {
                    existingVendor.setDescription(updatedVendor.getDescription());
                }
                if (updatedVendor.getImageUrl() != null) {
                    existingVendor.setImageUrl(updatedVendor.getImageUrl());
                }
                if (updatedVendor.getStatus() != null) {
                    existingVendor.setStatus(updatedVendor.getStatus());
                }
                return vendorRepository.save(existingVendor);
            })
            .orElseThrow(() -> new RuntimeException("Vendor not found"));
}

    public void addRatingToVendor(UUID vendorId, int newRating) {
    Vendor vendor = vendorRepository.findById(vendorId)
                    .orElseThrow(() -> new RuntimeException("Vendor not found"));

    int totalRatings = vendor.getTotalRatings();
    double currentAvg = vendor.getAverageRating();

    // Calculate new average
    double updatedAvg = ((currentAvg * totalRatings) + newRating) / (totalRatings + 1);

    vendor.setAverageRating(updatedAvg);
    vendor.setTotalRatings(totalRatings + 1);

    vendorRepository.save(vendor);
}



    // ✅ Delete a vendor by ID
    public void deleteVendor(UUID id) {
        vendorRepository.deleteById(id);
    }

    // ✅ Delete a vendor by name (custom)
    public void deleteVendorByName(String name) {
        vendorRepository.deleteByName(name);
    }

    // ✅ Get vendors by location
    public List<Vendor> getVendorsByLocation(String location) {
        return vendorRepository.findByLocation(location);
    }

    // ✅ Get vendors by status (Open/Closed)
    public List<Vendor> getVendorsByStatus(VendorStatus status) {
        return vendorRepository.findByStatus(status);
    }

    // ✅ Search vendors by keyword in name
    public List<Vendor> searchVendorsByName(String keyword) {
        return vendorRepository.findByNameContainingIgnoreCase(keyword);
    }
}



