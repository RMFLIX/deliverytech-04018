package com.deliveryth.delivery_api.config; 

import org.springframework.context.annotation.Bean; 
import org.springframework.context.annotation.Configuration; 
import org.springframework.http.HttpMethod; 
import org.springframework.security.config.annotation.web.builders.HttpSecurity; 
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; 
import org.springframework.security.web.SecurityFilterChain; 

@Configuration 
@EnableWebSecurity 
public class SecurityTestConfig { 
    
    @Bean 
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http 
        .csrf(csrf -> csrf.disable()) 
        .authorizeHttpRequests(auth -> auth 
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()  
            .requestMatchers(HttpMethod.POST, "/auth/**").permitAll() 
            .anyRequest().authenticated() 
        );

        return http.build();
    }
}