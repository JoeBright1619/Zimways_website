package auca.ac.rw.food.delivery.management.DTO;

import java.util.List;

public class DashboardStatsDTO {
    private double totalRevenue;
    private int totalOrders;
    private int activeCustomers;
    private int activeVendors;
    private List<TopSellingItemDTO> topSellingItems;

    // Getters and Setters
    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public int getActiveCustomers() {
        return activeCustomers;
    }

    public void setActiveCustomers(int activeCustomers) {
        this.activeCustomers = activeCustomers;
    }

    public int getActiveVendors() {
        return activeVendors;
    }

    public void setActiveVendors(int activeVendors) {
        this.activeVendors = activeVendors;
    }

    public List<TopSellingItemDTO> getTopSellingItems() {
        return topSellingItems;
    }

    public void setTopSellingItems(List<TopSellingItemDTO> topSellingItems) {
        this.topSellingItems = topSellingItems;
    }
}

class TopSellingItemDTO {
    private String name;
    private int unitsSold;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnitsSold() {
        return unitsSold;
    }

    public void setUnitsSold(int unitsSold) {
        this.unitsSold = unitsSold;
    }
} 