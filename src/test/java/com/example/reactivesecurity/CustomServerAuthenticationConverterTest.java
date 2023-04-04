package com.example.reactivesecurity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.mock.web.server.MockWebSession;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.server.ServerWebExchange;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository.DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME;

class CustomServerAuthenticationConverterTest {

    private final String headerName = "X_USER";

    private CustomServerAuthenticationConverter converter;

    @BeforeEach
    void beforeEach() {
        converter = new CustomServerAuthenticationConverter(headerName);
    }

    @Test
    public void shouldReturnNullAuthenticationObjectWhenASessionExists() {

        String userName = "USER1";

        MockServerHttpRequest request = MockServerHttpRequest.get("/")
                .header(headerName, userName)
                .build();

        MockWebSession session = new MockWebSession();
        session.getAttributes().put(DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME, "Test Value");

        ServerWebExchange serverWebExchange = MockServerWebExchange.builder(request)
                .session(session)
                .build();

        var authentication = converter.convert(serverWebExchange).block();
        assertThat(authentication).isNull();
    }

    @Test
    public void shouldReturnPreAuthenticatedAuthenticationToken() {

        String userName = "USER1";

        MockServerHttpRequest request = MockServerHttpRequest.get("/")
                .header(headerName, userName)
                .build();

        ServerWebExchange serverWebExchange = MockServerWebExchange.from(request);
        var authentication = converter.convert(serverWebExchange).block();

        assertThat(authentication.getPrincipal()).isEqualTo(userName);
        assertThat(authentication.getClass()).isEqualTo(PreAuthenticatedAuthenticationToken.class);

    }

    @Test
    public void shouldReturnNullAuthenticationObjectWhenHeaderIsMissing() {

        MockServerHttpRequest request = MockServerHttpRequest.get("/")
                .build();

        ServerWebExchange serverWebExchange = MockServerWebExchange.from(request);
        var authentication = converter.convert(serverWebExchange).block();

        assertThat(authentication).isNull();

    }

    @Test
    public void shouldReturnNullAuthenticationObjectWhenHeaderIsEmpty() {

        MockServerHttpRequest request = MockServerHttpRequest.get("/")
                .header(headerName, "")
                .build();

        ServerWebExchange serverWebExchange = MockServerWebExchange.from(request);
        var authentication = converter.convert(serverWebExchange).block();

        assertThat(authentication).isNull();
    }

}