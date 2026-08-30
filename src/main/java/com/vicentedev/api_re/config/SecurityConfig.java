package com.vicentedev.api_re.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @org.springframework.beans.factory.annotation.Value("${security.admin.username:admin}")
    private String adminUsername;

    @org.springframework.beans.factory.annotation.Value("${security.admin.password:admin123}")
    private String adminPassword;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/uploads/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/v1/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/v1/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(org.springframework.security.config.Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username(adminUsername)
                .password("{noop}" + adminPassword)
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
}
