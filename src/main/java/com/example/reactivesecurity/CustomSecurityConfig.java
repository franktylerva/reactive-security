package com.example.reactivesecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.*;

@EnableWebFluxSecurity
public class CustomSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http, AuthenticationWebFilter authenticationWebFilter,
            ReactiveUserDetailsService userDetailsService) {
        return http
                .formLogin().and() // use formLogin DSL which adds another AuthenticationWebFilter properly configured for form login
                .addFilterBefore(authenticationWebFilter, SecurityWebFiltersOrder.FORM_LOGIN)
                .authorizeExchange()
                .anyExchange().authenticated()
                .and()
                .build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User
                .withUsername("user2")
                .password("{noop}password")
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

    @Bean
    public AuthenticationWebFilter authenticationWebFilter() {

        var userDetailsService = new CustomUserDetailsService();
        var authenticationManager = new CustomPreAuthenticatedAuthenticationManager(userDetailsService);
        var filter = new AuthenticationWebFilter(authenticationManager);
        var converter = new CustomServerAuthenticationConverter("USER");
        filter.setServerAuthenticationConverter(converter);
//        var authenticationSuccessHandler = new CustomServerAuthenticationSuccessHandler();
//        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        return filter;
    }

}
