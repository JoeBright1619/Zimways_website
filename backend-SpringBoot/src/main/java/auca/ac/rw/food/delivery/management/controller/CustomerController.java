package auca.ac.rw.food.delivery.management.controller;

import auca.ac.rw.food.delivery.management.model.Customer;
import auca.ac.rw.food.delivery.management.service.CustomerService;
import auca.ac.rw.food.delivery.management.config.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;
import auca.ac.rw.food.delivery.management.DTO.CustomerDTO;

import java.io.PrintStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Optional<Customer> getCustomerById(@PathVariable UUID id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(customer));
        } catch (DataIntegrityViolationException e) {
            // Handle unique constraint violation
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A customer with this email and phone already exists!");
        } catch (Exception e) {
            // Handle other exceptions
             e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again.");
            
        }
    }


    @PostMapping("/login")
    public Customer loginCustomer(@RequestBody Customer loginRequest) {
        return customerService.loginCustomer(loginRequest.getEmail(), loginRequest.getPassword())
        .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
    }

    @PutMapping("{id}")
public ResponseEntity<?> updateCustomer(@PathVariable UUID id, @RequestBody CustomerDTO dto) {
    try {
        return customerService.updateCustomer(id, dto)
                .map(updated -> ResponseEntity.ok().body(updated))
                .orElseThrow(() -> new InvalidCredentialsException("customer doesn't exist"));
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to update customer");
    }
}

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomer(id);
    }
}



