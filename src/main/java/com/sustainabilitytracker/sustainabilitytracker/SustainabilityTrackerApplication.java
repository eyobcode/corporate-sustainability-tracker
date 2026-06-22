package com.sustainabilitytracker.sustainabilitytracker;

import com.sustainabilitytracker.sustainabilitytracker.entities.User;
import com.sustainabilitytracker.sustainabilitytracker.enums.Role;
import com.sustainabilitytracker.sustainabilitytracker.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SustainabilityTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SustainabilityTrackerApplication.class, args);
    }
    @Bean
    CommandLineRunner createDefaultUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String email = "eyob@dev.com";

            if (userRepository.findByEmail(email).isEmpty()) {
                User admin = new User();
                admin.setEmail(email);
                admin.setFullName("Eyob Dev");
                admin.setPassword(passwordEncoder.encode("password123"));
                admin.setRole(Role.ADMIN);
                admin.setIsActive(true);
                admin.setIsFirstLogin(true);

                userRepository.save(admin);
                System.out.println("✅ Default admin user created: " + email + " / password123");
            }
        };
    }
}
