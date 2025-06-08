package auca.ac.rw.food.delivery.management.controller;

import auca.ac.rw.food.delivery.management.model.Customer;
import auca.ac.rw.food.delivery.management.model.Order;
import auca.ac.rw.food.delivery.management.service.CustomerService;
import auca.ac.rw.food.delivery.management.service.PasswordResetService;
import auca.ac.rw.food.delivery.management.service.TwoFactorAuthService;
import auca.ac.rw.food.delivery.management.config.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;
import auca.ac.rw.food.delivery.management.DTO.CustomerDTO;
import auca.ac.rw.food.delivery.management.DTO.TwoFactorSetupDTO;
import auca.ac.rw.food.delivery.management.DTO.TwoFactorVerifyDTO;

import java.io.PrintStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final PasswordResetService passwordResetService;
    private final TwoFactorAuthService twoFactorAuthService;

    public CustomerController(CustomerService customerService, PasswordResetService passwordResetService, TwoFactorAuthService twoFactorAuthService) {
        this.customerService = customerService;
        this.passwordResetService = passwordResetService;
        this.twoFactorAuthService = twoFactorAuthService;
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
                    .body("A customer with this name and email already exists!");
        } catch (Exception e) {
            // Handle other exceptions
             e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again.");
            
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@RequestBody Customer loginRequest) {
        try {
            // First find the customer by email
            Optional<Customer> customerOpt = customerService.findByEmail(loginRequest.getEmail());
            
            if (!customerOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "No account found with this email"));
            }

            Customer customer = customerOpt.get();
            customer.setFirebaseToken(loginRequest.getFirebaseToken());
            
            Optional<Customer> verifiedCustomer = customerService.verifyAndLoginCustomer(customer);
            if (verifiedCustomer.isPresent()) {
                return ResponseEntity.ok(verifiedCustomer.get());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Login failed: " + e.getMessage()));
        }
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
    public ResponseEntity<?> deleteCustomer(@PathVariable UUID id) {
        try {
            // Check if customer exists
            Optional<Customer> customerOpt = customerService.getCustomerById(id);
            if (!customerOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Customer customer = customerOpt.get();
            
            // Clear relationships manually to avoid any potential issues
            if (customer.getOrders() != null) {
                for (Order order : customer.getOrders()) {
                    // Clear payment relationship if exists
                    if (order.getPayment() != null) {
                        order.getPayment().setOrder(null);
                        order.setPayment(null);
                    }
                    // Clear cart relationship if exists
                    if (order.getCart() != null) {
                        order.getCart().setOrder(null);
                        order.setCart(null);
                    }
                }
                customer.getOrders().clear();
            }

            // Clear cart relationship if exists
            if (customer.getCart() != null) {
                if (customer.getCart().getCartItems() != null) {
                    customer.getCart().getCartItems().clear();
                }
                if (customer.getCart().getOrder() != null) {
                    customer.getCart().getOrder().setCart(null);
                    customer.getCart().setOrder(null);
                }
                customer.setCart(null);
            }

            // Now delete the customer
            customerService.deleteCustomer(id);
            
            return ResponseEntity.ok()
                .body(Map.of("message", "Customer and related data deleted successfully"));
        } catch (Exception e) {
            e.printStackTrace(); // Log the full stack trace
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to delete customer: " + e.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            
            passwordResetService.createPasswordResetTokenForCustomer(email);
            return ResponseEntity.ok().body("Password reset instructions have been sent to your email");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            String newPassword = request.get("newPassword");
            
            if (token == null || token.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Token is required");
            }
            if (newPassword == null || newPassword.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("New password is required");
            }

            passwordResetService.validateTokenAndResetPassword(token, newPassword);
            return ResponseEntity.ok().body("Password has been reset successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/2fa/setup")
    public ResponseEntity<?> setup2FA(@RequestParam UUID customerId) {
        return customerService.getCustomerById(customerId)
                .map(customer -> {
                    String secret = twoFactorAuthService.generateNewSecret();
                    String qrCodeImage = twoFactorAuthService.generateQrCodeImageUri(secret, customer.getEmail());
                    return ResponseEntity.ok(new TwoFactorSetupDTO(qrCodeImage, secret));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/2fa/verify")
    public ResponseEntity<?> verify2FA(@RequestParam UUID customerId, @RequestBody TwoFactorVerifyDTO verifyDTO) {
        return customerService.getCustomerById(customerId)
                .map(customer -> {
                    if (twoFactorAuthService.verifyCode(verifyDTO.getCode(), verifyDTO.getSecret())) {
                        CustomerDTO dto = CustomerDTO.fromEntity(customer);
                        dto.setTfaSecret(verifyDTO.getSecret());
                        dto.setTfaEnabled(true);
                        customerService.updateCustomer(customerId, dto);
                        return ResponseEntity.ok().body(Map.of("message", "2FA enabled successfully"));
                    }
                    return ResponseEntity.badRequest().body(Map.of("message", "Invalid verification code"));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/2fa/disable")
    public ResponseEntity<?> disable2FA(@RequestParam UUID customerId, @RequestBody TwoFactorVerifyDTO verifyDTO) {
        return customerService.getCustomerById(customerId)
                .map(customer -> {
                    if (!customer.isTfaEnabled()) {
                        return ResponseEntity.badRequest().body(Map.of("message", "2FA is not enabled"));
                    }
                    if (twoFactorAuthService.verifyCode(verifyDTO.getCode(), customer.getTfaSecret())) {
                        CustomerDTO dto = CustomerDTO.fromEntity(customer);
                        dto.setTfaSecret(null);
                        dto.setTfaEnabled(false);
                        customerService.updateCustomer(customerId, dto);
                        return ResponseEntity.ok().body(Map.of("message", "2FA disabled successfully"));
                    }
                    return ResponseEntity.badRequest().body(Map.of("message", "Invalid verification code"));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/2fa/validate")
    public ResponseEntity<?> validate2FACode(@RequestParam UUID customerId, @RequestBody TwoFactorVerifyDTO verifyDTO) {
        return customerService.getCustomerById(customerId)
                .map(customer -> {
                    if (!customer.isTfaEnabled()) {
                        return ResponseEntity.ok().body(Map.of("valid", true, "message", "2FA not enabled"));
                    }
                    boolean isValid = twoFactorAuthService.verifyCode(verifyDTO.getCode(), customer.getTfaSecret());
                    return ResponseEntity.ok().body(Map.of("valid", isValid));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}



