package com.example.danceschool.security;

import com.example.danceschool.model.Korisnik;
import com.example.danceschool.service.KorisnikServis;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Optional;

/**
 * Konfiguraciona klasa za bezbednost aplikacije.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    /**
     * Bean za enkodiranje lozinki (koristi plain text - bez enkripcije).
     */
    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder password_encoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    
    /**
     * Servis za učitavanje korisničkih podataka.
     */
    @Bean
    public UserDetailsService user_details_service(KorisnikServis korisnik_servis) {
        return email -> {
            Optional<Korisnik> korisnik_opt = korisnik_servis.pronadji_korisnika_po_emailu(email);
            
            if (!korisnik_opt.isPresent()) {
                throw new UsernameNotFoundException("Korisnik nije pronađen: " + email);
            }
            
            Korisnik korisnik = korisnik_opt.get();
            
            return User.builder()
                .username(korisnik.getEmail())
                .password(korisnik.getLozinka())
                .roles(korisnik.getUloga())
                .build();
        };
    }
    
    /**
     * Konfiguriše lanac sigurnosnih filtera.
     */
    @Bean
    public SecurityFilterChain filter_chain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    new AntPathRequestMatcher("/"),
                    new AntPathRequestMatcher("/index"),
                    new AntPathRequestMatcher("/css/**"),
                    new AntPathRequestMatcher("/images/**"),
                    new AntPathRequestMatcher("/register"),
                    new AntPathRequestMatcher("/login"),
                    new AntPathRequestMatcher("/login**"),
                    new AntPathRequestMatcher("/logout"),
                    new AntPathRequestMatcher("/error"),
                    new AntPathRequestMatcher("/ws/**")
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
        
        return http.build();
    }
}
