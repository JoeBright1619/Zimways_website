package auca.ac.rw.food.delivery.management.controller;

import auca.ac.rw.food.delivery.management.DTO.LoginDTO;
import auca.ac.rw.food.delivery.management.model.Admin;
import auca.ac.rw.food.delivery.management.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ResponseEntity<Admin> login(@RequestBody LoginDTO loginDTO) {
        System.out.println("Received login request for admin: " + loginDTO.getIdentifier());
        return ResponseEntity.ok(adminService.login(loginDTO.getIdentifier(), loginDTO.getPassword()));
    }
} 