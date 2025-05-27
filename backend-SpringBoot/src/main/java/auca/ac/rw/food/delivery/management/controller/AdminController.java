package auca.ac.rw.food.delivery.management.controller;

import auca.ac.rw.food.delivery.management.DTO.LoginDTO;
import auca.ac.rw.food.delivery.management.DTO.DashboardStatsDTO;
import auca.ac.rw.food.delivery.management.DTO.RevenueStatsDTO;
import auca.ac.rw.food.delivery.management.DTO.VendorPerformanceDTO;
import auca.ac.rw.food.delivery.management.model.Admin;
import auca.ac.rw.food.delivery.management.model.Order;
import auca.ac.rw.food.delivery.management.service.AdminService;
import auca.ac.rw.food.delivery.management.service.AdminDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {
    private final AdminService adminService;
    private final AdminDashboardService dashboardService;

    public AdminController(AdminService adminService, AdminDashboardService dashboardService) {
        this.adminService = adminService;
        this.dashboardService = dashboardService;
    }

    @PostMapping("/login")
    public ResponseEntity<Admin> login(@RequestBody LoginDTO loginDTO) {
        System.out.println("Received login request for admin: " + loginDTO.getIdentifier());
        return ResponseEntity.ok(adminService.login(loginDTO.getIdentifier(), loginDTO.getPassword()));
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }

    @GetMapping("/orders/recent")
    public ResponseEntity<List<Order>> getRecentOrders(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(dashboardService.getRecentOrders(limit));
    }

    @GetMapping("/revenue")
    public ResponseEntity<RevenueStatsDTO> getRevenueStats(@RequestParam(defaultValue = "month") String period) {
        return ResponseEntity.ok(dashboardService.getRevenueStats(period));
    }

    @GetMapping("/vendors/performance")
    public ResponseEntity<List<VendorPerformanceDTO>> getVendorPerformance() {
        return ResponseEntity.ok(dashboardService.getVendorPerformance());
    }
} 