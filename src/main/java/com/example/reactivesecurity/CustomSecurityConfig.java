package com.example.reactivesecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.*;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

@EnableWebFluxSecurity
public class CustomSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         AuthenticationWebFilter authenticationWebFilter) {
        return http
                .formLogin().and()
                .addFilterBefore(authenticationWebFilter, SecurityWebFiltersOrder.FORM_LOGIN)
                .authorizeExchange()
                .anyExchange().authenticated()
                .and()
                .securityContextRepository(securityContextRepository())
                .build();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public AuthenticationWebFilter authenticationWebFilter(ServerSecurityContextRepository securityContextRepository,
                                                           ReactiveUserDetailsService userDetailsService) {

        var authenticationFilter = new AuthenticationWebFilter(
                new ReactivePreAuthenticatedAuthenticationManager(userDetailsService));

        authenticationFilter.setServerAuthenticationConverter(
                new CustomServerAuthenticationConverter("USER"));

        authenticationFilter.setSecurityContextRepository(securityContextRepository);

        return authenticationFilter;
    }
    @Bean
    ServerSecurityContextRepository securityContextRepository() {

        var securityContextRepository = new WebSessionServerSecurityContextRepository();
        securityContextRepository.setCacheSecurityContext(true);
        return securityContextRepository;
    }

}
