package com.example.reactivesecurity;

import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import reactor.core.publisher.Mono;

public class CustomPreAuthenticatedAuthenticationManager implements ReactiveAuthenticationManager {

    private final ReactiveUserDetailsService userDetailsService;
    private final UserDetailsChecker userDetailsChecker;

    public CustomPreAuthenticatedAuthenticationManager(ReactiveUserDetailsService userDetailsService) {
        this(userDetailsService, new AccountStatusUserDetailsChecker());
    }

    public CustomPreAuthenticatedAuthenticationManager(ReactiveUserDetailsService userDetailsService, UserDetailsChecker userDetailsChecker) {
        this.userDetailsService = userDetailsService;
        this.userDetailsChecker = userDetailsChecker;
    }
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        // Call user details service and return a custom authentication object
        // Set newUser if user details service returns and empty Mono
        return Mono.just(authentication).filter(this::supports).map(Authentication::getName)
                .flatMap(this.userDetailsService::findByUsername)
                .switchIfEmpty(Mono.error(() -> new UsernameNotFoundException("User not found")))
                .doOnNext(this.userDetailsChecker::check).map((userDetails) -> {
                    CustomAuthenticationToken result = new CustomAuthenticationToken(userDetails,
                            authentication.getCredentials(), userDetails.getAuthorities());
                    result.setDetails(authentication.getDetails());
                    return result;
                });
    }

    private boolean supports(Authentication authentication) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication.getClass());
    }
}
