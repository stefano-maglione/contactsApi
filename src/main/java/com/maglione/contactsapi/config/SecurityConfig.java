package com.maglione.contactsapi.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain swaggerDocumentationSecurity(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")

                        .permitAll()
                );

        return httpSecurity.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain restApiSecurity(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .authorizeHttpRequests(requests -> requests
                        .anyRequest()
                        .authenticated()

                ).httpBasic(httpSecurityHttpBasicConfigurer -> {
                    httpSecurityHttpBasicConfigurer.realmName("Apicontacts");

                }).csrf().disable().build();
    }

}
