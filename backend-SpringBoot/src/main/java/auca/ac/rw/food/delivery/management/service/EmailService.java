package auca.ac.rw.food.delivery.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, please use this code: " + token + "\n\n" +
                "This code will expire in 1 hour.\n\n" +
                "If you did not request a password reset, please ignore this email.");

        mailSender.send(message);
    }
} 