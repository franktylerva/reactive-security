package com.example.reactivesecurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

import java.net.URI;

public class CustomAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    RedirectServerAuthenticationSuccessHandler defaultHandler =
            new RedirectServerAuthenticationSuccessHandler("/");

    ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {

        var request = webFilterExchange.getExchange().getRequest();
        var response = webFilterExchange.getExchange().getResponse();

        if (authentication instanceof CustomAuthenticationToken token) {
            if (token.getDetails() instanceof CustomUserDetail details) {
                if(details.isNewUser()) {
                    return redirectStrategy.sendRedirect(webFilterExchange.getExchange(), URI.create("/"));
                }
            }
        }

        return defaultHandler.onAuthenticationSuccess(webFilterExchange, authentication);
    }
}
