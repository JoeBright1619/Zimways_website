package auca.ac.rw.food.delivery.management.service;

import auca.ac.rw.food.delivery.management.DTO.DashboardStatsDTO;
import auca.ac.rw.food.delivery.management.DTO.RevenueStatsDTO;
import auca.ac.rw.food.delivery.management.DTO.VendorPerformanceDTO;
import auca.ac.rw.food.delivery.management.model.Order;
import auca.ac.rw.food.delivery.management.model.Vendor;
import auca.ac.rw.food.delivery.management.model.enums.OrderStatus;
import auca.ac.rw.food.delivery.management.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminDashboardService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final VendorRepository vendorRepository;
    private final ItemRepository itemRepository;

    public AdminDashboardService(OrderRepository orderRepository,
                               CustomerRepository customerRepository,
                               VendorRepository vendorRepository,
                               ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;
        this.itemRepository = itemRepository;
    }

    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        
        // Set total revenue from completed orders
        List<Order> completedOrders = orderRepository.findByStatus(OrderStatus.COMPLETED);
        double totalRevenue = completedOrders != null ? 
            completedOrders.stream()
                .filter(order -> order.getTotal() != null)
                .mapToDouble(Order::getTotal)
                .sum() : 0.0;
        
        stats.setTotalRevenue(totalRevenue);
        stats.setTotalOrders((int) orderRepository.count());
        stats.setActiveCustomers((int) customerRepository.count());
        stats.setActiveVendors((int) vendorRepository.count());
        stats.setTopSellingItems(new ArrayList<>());
        
        return stats;
    }

    public List<Order> getRecentOrders(int limit) {
        List<Order> orders = orderRepository.findTop5ByOrderByOrderDateDesc();
        return orders != null ? orders : new ArrayList<>();
    }

    public RevenueStatsDTO getRevenueStats(String period) {
        RevenueStatsDTO stats = new RevenueStatsDTO();
        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");
        
        List<Order> completedOrders = orderRepository.findByStatus(OrderStatus.COMPLETED);
        if (completedOrders == null) {
            completedOrders = new ArrayList<>();
        }
        
        // Last 7 days revenue
        for (int i = 6; i >= 0; i--) {
            LocalDateTime date = now.minusDays(i);
            labels.add(date.format(formatter));
            
            final LocalDateTime currentDate = date;
            double dailyRevenue = completedOrders.stream()
                    .filter(order -> order.getOrderDate() != null && 
                            order.getTotal() != null &&
                            order.getOrderDate().toLocalDate().equals(currentDate.toLocalDate()))
                    .mapToDouble(Order::getTotal)
                    .sum();
            
            data.add(dailyRevenue);
        }
        
        stats.setLabels(labels);
        stats.setData(data);
        
        return stats;
    }

    public List<VendorPerformanceDTO> getVendorPerformance() {
        List<Vendor> vendors = vendorRepository.findAll();
        if (vendors == null) {
            return new ArrayList<>();
        }

        List<Order> completedOrders = orderRepository.findByStatus(OrderStatus.COMPLETED);
        if (completedOrders == null) {
            completedOrders = new ArrayList<>();
        }

        final List<Order> finalCompletedOrders = completedOrders;

        return vendors.stream()
                .map(vendor -> {
                    VendorPerformanceDTO dto = new VendorPerformanceDTO();
                    dto.setId(vendor.getId());
                    dto.setName(vendor.getName());
                    
                    double totalSales = finalCompletedOrders.stream()
                            .filter(order -> order.getTotal() != null)
                            .mapToDouble(Order::getTotal)
                            .sum();
                    
                    dto.setTotalSales(totalSales);
                    dto.setTotalOrders(finalCompletedOrders.size());
                    dto.setRating(4.5); // Placeholder - implement based on your rating system
                    
                    return dto;
                })
                .collect(Collectors.toList());
    }
} 