package com.example.danceschool.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity (enable in production)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Allow all requests without authentication
            )
            .formLogin(form -> form.disable()) // Disable Spring Security's default login form
            .httpBasic(basic -> basic.disable()); // Disable HTTP Basic authentication
        
        return http.build();
    }
}
