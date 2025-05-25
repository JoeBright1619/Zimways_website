package auca.ac.rw.food.delivery.management.service;

import auca.ac.rw.food.delivery.management.model.Cart;
import auca.ac.rw.food.delivery.management.model.Customer;
import auca.ac.rw.food.delivery.management.repository.*;
import org.springframework.stereotype.Service;

import auca.ac.rw.food.delivery.management.DTO.CustomerDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service  // ✅ This tells Spring that this class is a service component
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    
    public CustomerService(CustomerRepository customerRepository, CartRepository cartRepository) {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(UUID id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> loginCustomer(String email, String password) {
        return customerRepository.findByEmailAndPassword(email, password);
    }

    public Customer createCustomer(Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
    
        Cart cart = new Cart();
        cart.setCustomer(savedCustomer);
        cartRepository.save(cart);
        
        savedCustomer.setCart(cart);
        return customerRepository.save(savedCustomer);
    }

    public Optional<Customer> updateCustomer(UUID id, CustomerDTO dto) {
        return customerRepository.findById(id).map(existingCustomer -> {
            if (dto.getName() != null) {
                existingCustomer.setName(dto.getName());
            }
            if (dto.getPhone() != null) {
                existingCustomer.setPhone(dto.getPhone());
            }
            if (dto.getPassword() != null) {
                existingCustomer.setPassword(dto.getPassword());
            }
            // Handle 2FA fields
            existingCustomer.setTfaSecret(dto.getTfaSecret());
            existingCustomer.setTfaEnabled(dto.isTfaEnabled());

            return customerRepository.save(existingCustomer);
        });
    }

    public void deleteCustomer(UUID id) {
        customerRepository.deleteById(id);
    }
}
