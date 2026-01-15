package com.example.restservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Security configuration class for the REST Service.
 * Configures authentication, authorization, and security filters.
 * Implements Basic Authentication with in-memory user details for
 * demonstration.
 * 
 * @author Application Development Team
 * @since 1.0
 */
@Configuration
@EnableWebMvc
public class SecurityConfig {
    /**
     * Configures the security filter chain.
     * Disables CSRF protection, configures role-based access control,
     * and enables Basic HTTP Authentication.
     * 
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for POST/PUT/DELETE requests
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasAnyRole("USER", "ADMIN") // Secure PUT
                                                                                                  // /users/**
                        .anyRequest().permitAll()) // Allow other endpoints
                .httpBasic(basic -> {
                }); // Use basic auth
        return http.build();
    }

    /**
     * Provides the password encoder bean using BCrypt.
     * 
     * @return BCryptPasswordEncoder for secure password hashing
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides the user details service with in-memory users.
     * Configures two demo users: 'dikwan' and 'admin' with different roles.
     * 
     * @param passwordEncoder the password encoder to use for encoding user
     *                        passwords
     * @return the configured UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .username("dikwan")
                .password(passwordEncoder.encode("password123"))
                .roles("USER", "VIEWER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("password123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);

    }
}
