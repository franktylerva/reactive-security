package com.example.reactivesecurity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuditLogger {

    private static final Logger auditLog = LoggerFactory.getLogger("mctims-audit-log");

    public void logRequest(ServerHttpRequest request, ServerHttpResponse response, Authentication authentication) {

        var user = authentication != null ? authentication.getName() : "";
        var ip = getRemoteAddr(request);
        var method = request.getMethod().toString();
        var path = request.getPath().value();
        var status = response.getStatusCode().toString();

        logRequest(user, ip, method, path, status);
    }

    public void logRequest(String user, String ip, String method, String path, String status) {
        auditLog.info("{} {} {} {} {}", user, ip, method, path, status);
    }

    private String getRemoteAddr(ServerHttpRequest request) {
        var ipFromHeader = request.getHeaders().get("X-FORWARDED-FOR");
        if (ipFromHeader != null && ipFromHeader.size() > 0) {
            return ipFromHeader.get(0);
        }
        return request.getRemoteAddress().toString();
    }
}
