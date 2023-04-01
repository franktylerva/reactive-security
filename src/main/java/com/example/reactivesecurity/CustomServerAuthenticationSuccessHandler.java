package com.example.reactivesecurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

public class CustomServerAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    private RedirectServerAuthenticationSuccessHandler defaultHandler =
            new RedirectServerAuthenticationSuccessHandler("/");

    private ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {

        var request = webFilterExchange.getExchange().getRequest();
        var response = webFilterExchange.getExchange().getResponse();

        return defaultHandler.onAuthenticationSuccess(webFilterExchange, authentication);
    }
}
