package auca.ac.rw.food.delivery.management.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String[] passwords = {"bright", "fofo", "kirenga"};
        
        for (String password : passwords) {
            String hashedPassword = encoder.encode(password);
            System.out.println("Password: " + password);
            System.out.println("BCrypt Hash: " + hashedPassword);
            System.out.println();
        }
    }
} 