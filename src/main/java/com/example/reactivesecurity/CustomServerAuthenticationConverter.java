package com.example.reactivesecurity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class CustomServerAuthenticationConverter implements ServerAuthenticationConverter {

    private static final Logger log = LoggerFactory.getLogger(CustomServerAuthenticationConverter.class);

    private final String HEADER_NAME;

    public CustomServerAuthenticationConverter(String HEADER_NAME) {
        this.HEADER_NAME = HEADER_NAME;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {

        var headers = exchange.getRequest().getHeaders().get(HEADER_NAME);
        if(headers != null) {
            var userName = exchange.getRequest().getHeaders().get(HEADER_NAME).get(0);
            if(!userName.trim().isBlank()) {
                log.debug("ServerWebExchange converted to PreAuthenticatedAuthenticationToken....");
                return Mono.just(new PreAuthenticatedAuthenticationToken(userName, "bogus"));
            }
        }
        return Mono.empty();
    }
}
