package com.example.tasksupport.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.tasksupport.entity.User;
import com.example.tasksupport.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (userRepository.findByUsername("admin").isEmpty()) {

            User user = new User();

            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRole("ADMIN");

            userRepository.save(user);
        }

        if (userRepository.findByUsername("user").isEmpty()) {

            User user = new User();

            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRole("USER");

            userRepository.save(user);
        }
    }
}