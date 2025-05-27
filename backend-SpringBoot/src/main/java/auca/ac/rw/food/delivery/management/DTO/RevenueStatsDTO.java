package auca.ac.rw.food.delivery.management.DTO;

import java.util.List;

public class RevenueStatsDTO {
    private List<String> labels;
    private List<Double> data;

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Double> getData() {
        return data;
    }

    public void setData(List<Double> data) {
        this.data = data;
    }
} 