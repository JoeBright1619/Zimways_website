package auca.ac.rw.food.delivery.management.DTO;

import auca.ac.rw.food.delivery.management.model.Customer;

public class CustomerDTO {
    private String name;
    private String phone;
    private String password;
    private String tfaSecret;
    private boolean tfaEnabled;

    // Static factory method to create DTO from entity
    public static CustomerDTO fromEntity(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setName(customer.getName());
        dto.setPhone(customer.getPhone());
        dto.setPassword(customer.getPassword());
        dto.setTfaSecret(customer.getTfaSecret());
        dto.setTfaEnabled(customer.isTfaEnabled());
        return dto;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTfaSecret() { return tfaSecret; }
    public void setTfaSecret(String tfaSecret) { this.tfaSecret = tfaSecret; }

    public boolean isTfaEnabled() { return tfaEnabled; }
    public void setTfaEnabled(boolean tfaEnabled) { this.tfaEnabled = tfaEnabled; }
}
