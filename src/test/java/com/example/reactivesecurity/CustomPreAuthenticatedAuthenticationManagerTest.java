package com.example.reactivesecurity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomPreAuthenticatedAuthenticationManagerTest {

    private CustomPreAuthenticatedAuthenticationManager authenticationManager;

    @BeforeEach
    void beforeEach() {
        authenticationManager = new CustomPreAuthenticatedAuthenticationManager(
                new CustomUserDetailsService());
    }
    @Test
    void shouldReturnCustomAuthenticationToken() {

        var userName = "user1";
        var preAuthenticatedAuthenticationToken = new PreAuthenticatedAuthenticationToken(userName, "");

        var result = authenticationManager.authenticate(preAuthenticatedAuthenticationToken).block();

        assertThat(result.isAuthenticated()).isTrue();
        assertThat(result.getName()).isEqualTo(userName);
        assertThat(result.getClass()).isEqualTo(CustomAuthenticationToken.class);
    }

    @Test
    void shouldThrowUsernameNotFoundException() {

        var userName = "user2";
        var preAuthenticatedAuthenticationToken = new PreAuthenticatedAuthenticationToken(userName, "");
        var userDetails = new CustomUserDetail();
        userDetails.setUserName(userName);
        userDetails.setFirstName("John");
        userDetails.setLastName("Doe");

        assertThrows(UsernameNotFoundException.class, () -> {
            authenticationManager.authenticate(preAuthenticatedAuthenticationToken).block();
        });

    }

}