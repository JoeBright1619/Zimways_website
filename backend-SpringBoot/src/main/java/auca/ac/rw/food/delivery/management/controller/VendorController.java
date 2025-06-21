package auca.ac.rw.food.delivery.management.controller;

import auca.ac.rw.food.delivery.management.DTO.LoginDTO;
import auca.ac.rw.food.delivery.management.DTO.VendorDTO;
import auca.ac.rw.food.delivery.management.model.Vendor;
import auca.ac.rw.food.delivery.management.model.enums.VendorStatus;
import auca.ac.rw.food.delivery.management.service.VendorService;
import auca.ac.rw.food.delivery.management.config.InvalidCredentialsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vendors")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    public List<Vendor> getAllVendors() {
        return vendorService.getAllVendors();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getVendorById(@PathVariable UUID id) {
        return vendorService.getVendorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
public ResponseEntity<List<Vendor>> searchVendorsByName(@RequestParam(defaultValue = "") String keyword) {
    List<Vendor> vendors = vendorService.searchVendorsByName(keyword);
    return ResponseEntity.ok(vendors);
}


    @GetMapping("/status/{status}")
    public List<Vendor> getVendorsByStatus(@PathVariable VendorStatus status) {
        return vendorService.getVendorsByStatus(status);
    }

    @GetMapping("/location/{location}")
    public List<Vendor> getVendorsByLocation(@PathVariable String location) {
        return vendorService.getVendorsByLocation(location);
    }

    @PostMapping
    public ResponseEntity<Vendor> createVendor(@RequestBody VendorDTO vendor) {
        return ResponseEntity.ok(vendorService.createVendor(vendor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vendor> updateVendor(@PathVariable UUID id, @RequestBody VendorDTO vendorDTO) {
        return ResponseEntity.ok(vendorService.updateVendor(id, vendorDTO));
    }

    @PostMapping("/{id}/rating")
    public ResponseEntity<Void> addRatingToVendor(@PathVariable UUID id, @RequestParam int rating) {
        vendorService.addRatingToVendor(id, rating);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable UUID id) {
        vendorService.deleteVendor(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/name/{name}")
    public ResponseEntity<Void> deleteVendorByName(@PathVariable String name) {
        vendorService.deleteVendorByName(name);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Vendor> login(@RequestBody LoginDTO loginDTO) {
        try {
            Vendor vendor = vendorService.login(loginDTO.getIdentifier(), loginDTO.getPassword());
            return ResponseEntity.ok(vendor);
        } catch (RuntimeException e) {
            throw new InvalidCredentialsException(e.getMessage());
        }
    }
}
