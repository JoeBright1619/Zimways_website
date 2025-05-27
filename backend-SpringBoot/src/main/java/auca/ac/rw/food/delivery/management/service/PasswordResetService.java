package auca.ac.rw.food.delivery.management.service;

import auca.ac.rw.food.delivery.management.model.Customer;
import auca.ac.rw.food.delivery.management.model.PasswordResetToken;
import auca.ac.rw.food.delivery.management.repository.PasswordResetTokenRepository;
import auca.ac.rw.food.delivery.management.repository.CustomerRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final CustomerRepository customerRepository;
    private final EmailService emailService;

    public PasswordResetService(
            PasswordResetTokenRepository tokenRepository,
            CustomerRepository customerRepository,
            EmailService emailService) {
        this.tokenRepository = tokenRepository;
        this.customerRepository = customerRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void createPasswordResetTokenForCustomer(String email) {
        Optional<Customer> customerOpt = customerRepository.findByEmail(email);
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("No customer found with email: " + email);
        }

        Customer customer = customerOpt.get();
        
        // Delete any existing tokens for this customer
        tokenRepository.deleteByCustomer(customer);

        // Generate new token
        String token = RandomStringUtils.randomNumeric(6);
        PasswordResetToken resetToken = new PasswordResetToken(token, customer);
        tokenRepository.save(resetToken);

        // Send email
        emailService.sendPasswordResetEmail(customer.getEmail(), token);
    }

    @Transactional
    public void validateTokenAndResetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        if (tokenOpt.isEmpty()) {
            throw new RuntimeException("Invalid reset token");
        }

        PasswordResetToken resetToken = tokenOpt.get();
        if (resetToken.isExpired()) {
            throw new RuntimeException("Token has expired");
        }

        if (resetToken.isUsed()) {
            throw new RuntimeException("Token has already been used");
        }

        Customer customer = resetToken.getCustomer();
        customer.setPassword(newPassword); // Note: In real application, password should be hashed
        customerRepository.save(customer);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }
} 