package com.um.springbootprojstructure.config;

import com.um.springbootprojstructure.entity.User;
import com.um.springbootprojstructure.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User alice = new User();
            alice.setUsername("alice");
            alice.setEmail("alice@example.com");
            alice.setPasswordHash(passwordEncoder.encode("Password123!"));
            alice.setActive(true);
            alice.setRoles("USER");
            userRepository.save(alice);

            User bob = new User();
            bob.setUsername("bob");
            bob.setEmail("bob@example.com");
            bob.setPasswordHash(passwordEncoder.encode("Password123!"));
            bob.setActive(true);
            bob.setRoles("USER");
            userRepository.save(bob);
        }
    }
}
