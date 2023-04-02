package com.example.reactivesecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.*;

@EnableWebFluxSecurity
public class CustomSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .formLogin().and() // use formLogin DSL which adds another AuthenticationWebFilter properly configured for form login
                .addFilterAt(authenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange()
                .anyExchange().authenticated()
                .and()
                .build();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public AuthenticationWebFilter authenticationWebFilter() {

        var authenticationManager = new ReactivePreAuthenticatedAuthenticationManager(userDetailsService());
        var filter = new AuthenticationWebFilter(authenticationManager);
        var converter = new CustomServerAuthenticationConverter("USER");
        filter.setServerAuthenticationConverter(converter);
        return filter;
    }

}
