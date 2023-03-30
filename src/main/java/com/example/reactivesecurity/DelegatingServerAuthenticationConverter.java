package com.example.reactivesecurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

public class DelegatingServerAuthenticationConverter implements ServerAuthenticationConverter {

    private final List<ServerAuthenticationConverter> delegates;

    public DelegatingServerAuthenticationConverter(ServerAuthenticationConverter... converters) {
        this(Arrays.asList(converters));
    }

    public DelegatingServerAuthenticationConverter(List<ServerAuthenticationConverter> converters) {
        this.delegates = converters;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {

        return Flux.fromIterable(this.delegates)
                .concatMap((m) -> m.convert(exchange))
                .next();
    }
}
