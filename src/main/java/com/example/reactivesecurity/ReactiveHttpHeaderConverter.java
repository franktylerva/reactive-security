package com.example.reactivesecurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class ReactiveHttpHeaderConverter implements ServerAuthenticationConverter {

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {

        var headers = exchange.getRequest().getHeaders().get("USER");
        if(headers != null) {
            var userName = exchange.getRequest().getHeaders().get("USER").get(0);
            return Mono.just(new PreAuthenticatedAuthenticationToken(userName, null));
        }
        return Mono.empty();
    }
}
