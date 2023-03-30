package com.example.reactivesecurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final AuditLogger auditLogger;

    private RedirectServerAuthenticationSuccessHandler defaultHandler =
            new RedirectServerAuthenticationSuccessHandler("/");

    private ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();

    public AuthenticationSuccessHandler(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {

        var request = webFilterExchange.getExchange().getRequest();
        var response = webFilterExchange.getExchange().getResponse();
        auditLogger.logRequest(request, response, authentication);

        return defaultHandler.onAuthenticationSuccess(webFilterExchange, authentication);
    }
}
