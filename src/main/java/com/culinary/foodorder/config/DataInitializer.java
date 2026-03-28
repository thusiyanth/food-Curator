package com.culinary.foodorder.config;

import com.culinary.foodorder.entity.User;
import com.culinary.foodorder.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Ensure admin user exists with correct password hash
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
            log.info("Created default admin user (admin / admin123)");
        } else {
            // Update password to ensure it matches
            userRepository.findByUsername("admin").ifPresent(user -> {
                if (!passwordEncoder.matches("admin123", user.getPassword())) {
                    user.setPassword(passwordEncoder.encode("admin123"));
                    userRepository.save(user);
                    log.info("Updated admin user password hash");
                }
            });
        }
    }
}
