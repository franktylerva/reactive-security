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
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http, AuthenticationWebFilter authenticationWebFilter,
            ReactiveUserDetailsService userDetailsService) {
        return http
                .addFilterBefore(authenticationWebFilter, SecurityWebFiltersOrder.FORM_LOGIN)
                .authorizeExchange()
                .anyExchange().authenticated()
                .and()
                .build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User
                .withUsername("user1")
                .password("{noop}password")
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService userDetailsService) {
       return new ReactivePreAuthenticatedAuthenticationManager(userDetailsService);
    }
    @Bean
    public AuthenticationWebFilter authenticationWebFilter(ReactiveAuthenticationManager authenticationManager,
                                                           ServerAuthenticationConverter converter) {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(authenticationManager);
        filter.setServerAuthenticationConverter(converter);
        return filter;
    }

    @Bean
    public ServerAuthenticationConverter converters() {
        return new DelegatingServerAuthenticationConverter(new ReactiveHttpHeaderConverter(),
                new ServerFormLoginAuthenticationConverter());
    }

}
