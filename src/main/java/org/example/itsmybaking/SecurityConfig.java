package org.example.itsmybaking;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest().permitAll() // Allows access to all URLs without authentication
                )
                .csrf(csrf -> csrf.disable()); // Disable CSRF protection without using deprecated methods

        return http.build();
    }
    //CSRF (Cross-Site Request Forgery):
//    CSRF is a type of security vulnerability where an attacker tricks a user's browser into making unwanted requests to a web application where the user is authenticated. This can lead to unauthorized actions being performed on behalf of the user.

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
