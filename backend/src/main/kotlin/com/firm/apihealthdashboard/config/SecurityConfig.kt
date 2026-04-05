package com.firm.apihealthdashboard.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() } // Will be configured with SAML in Story 1.2
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/v1/health", "/actuator/health").permitAll()
                    .anyRequest().authenticated()
            }
        return http.build()
    }
}
