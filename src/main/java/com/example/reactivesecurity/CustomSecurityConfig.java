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
                .withUsername("user1")
                .password("{noop}password")
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

    @Bean
    public AuthenticationWebFilter authenticationWebFilter(ReactiveUserDetailsService userDetailsService,
                                                           ServerAuthenticationConverter converter) {
        // inline the ReactivePreAuthenticatedAuthenticationManager which should only be used for pre authentication
        // This ensures that formLogin uses the MapReactiveUserDetailsService to create a
        // UserDetailsRepositoryReactiveAuthenticationManager
        ReactiveAuthenticationManager authenticationManager = new ReactivePreAuthenticatedAuthenticationManager(userDetailsService);
        AuthenticationWebFilter filter = new AuthenticationWebFilter(authenticationManager);
        filter.setServerAuthenticationConverter(converter);
        //filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        return filter;
    }

    @Bean
    public ServerAuthenticationConverter converters() {
        return new CustomServerAuthenticationConverter("USER");
    }

}
