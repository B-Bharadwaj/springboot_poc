package com.example.order_poc.service;

import com.example.order_poc.config.AppProperties;
import com.example.order_poc.dto.AuthResponse;
import com.example.order_poc.dto.LoginRequest;
import com.example.order_poc.dto.RegisterRequest;
import com.example.order_poc.dto.UserResponse;
import com.example.order_poc.exception.EmailAlreadyUsedException;
import com.example.order_poc.exception.InvalidCredentialsException;
import com.example.order_poc.model.User;
import com.example.order_poc.repository.UserRepository;
import com.example.order_poc.security.JwtService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

// Service layer: creates accounts and issues login tokens.
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AppProperties appProperties;
    // Hash checked when the email is unknown, so timing matches.
    private final String dummyHash;

    // Spring passes in repository, encoder, JWT service, config.
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, AppProperties appProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.appProperties = appProperties;
        this.dummyHash = passwordEncoder.encode("timing-protection-placeholder");
    }

    // Read-write transaction: SELECT to check email, then INSERT.
    @Transactional
    public UserResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.getEmail());
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyUsedException("Email is already registered: " + email);
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(email);
        // Store only the BCrypt hash, never the plain password.
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        try {
            // Flush now so a duplicate hits the unique index here.
            return toResponse(userRepository.saveAndFlush(user));
        } catch (DataIntegrityViolationException e) {
            // Two parallel registrations with the same email.
            throw new EmailAlreadyUsedException("Email is already registered: " + email);
        }
    }

    // Read-only transaction: one SELECT by email.
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(normalizeEmail(request.getEmail())).orElse(null);
        // Seeded users have no hash, so they cannot log in.
        String hash = (user != null && user.getPasswordHash() != null) ? user.getPasswordHash() : dummyHash;
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), hash);
        if (user == null || user.getPasswordHash() == null || !passwordMatches) {
            throw new InvalidCredentialsException();
        }
        String token = jwtService.generateToken(user.getEmail(), user.getRole());
        return new AuthResponse(token, "Bearer", appProperties.jwt().expirationMinutes());
    }

    // " Test@Example.com " becomes "test@example.com".
    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
