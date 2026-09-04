package com.vicentedev.api_re.config;

import com.vicentedev.api_re.entity.Role;
import com.vicentedev.api_re.entity.User;
import com.vicentedev.api_re.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.default-name}")
    private String defaultAdminName;

    @Value("${app.admin.default-email}")
    private String defaultAdminEmail;

    @Value("${app.admin.default-password}")
    private String defaultAdminPassword;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String normalizedEmail = defaultAdminEmail.trim().toLowerCase();
        userRepository.findByEmailIgnoreCase(normalizedEmail).ifPresentOrElse(
                existingAdmin -> {
                    existingAdmin.setName(defaultAdminName);
                    existingAdmin.setPassword(passwordEncoder.encode(defaultAdminPassword));
                    existingAdmin.setRole(Role.ROLE_ADMIN);
                    userRepository.save(existingAdmin);
                    log.info("Default administrator account synced with environment credentials: {}", normalizedEmail);
                },
                () -> {
                    User admin = User.builder()
                            .name(defaultAdminName)
                            .email(normalizedEmail)
                            .password(passwordEncoder.encode(defaultAdminPassword))
                            .role(Role.ROLE_ADMIN)
                            .lastLoginAt(OffsetDateTime.now())
                            .build();

                    userRepository.save(admin);
                    log.info("Default administrator account initialized: {}", normalizedEmail);
                }
        );
    }
}
