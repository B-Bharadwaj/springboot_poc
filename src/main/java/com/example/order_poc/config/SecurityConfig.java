package com.example.order_poc.config;

import com.example.order_poc.security.JwtAuthenticationFilter;
import com.example.order_poc.security.JwtService;
import com.example.order_poc.security.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Security rules: public paths, JWT filter, stateless sessions.
// Marks a class whose @Bean methods create Spring beans.
@Configuration
public class SecurityConfig {

    // Return value becomes a Spring bean other classes can inject.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtService jwtService,
                                                   RestAuthenticationEntryPoint entryPoint) {
        http
                // No cookies or forms, so CSRF protection is not needed.
                .csrf(csrf -> csrf.disable())
                // No HTTP session; every request carries its token.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/swagger-ui/**", "/swagger-ui.html",
                                "/v3/api-docs/**", "/actuator/health", "/hello").permitAll()
                        .anyRequest().authenticated())
                // Missing or bad token on a protected path: 401 JSON.
                .exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint))
                // Check the JWT before the username/password login filter.
                .addFilterBefore(new JwtAuthenticationFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // BCrypt: slow, salted hashing for stored passwords.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
