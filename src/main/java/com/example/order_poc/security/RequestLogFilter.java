package com.example.order_poc.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// Logs one [REQ] and one [RES] line per call; never headers, tokens or bodies.
// Not a @Component, so it only runs inside the security chain.
// Lombok adds a "log" field for this class.
@Slf4j
public class RequestLogFilter extends OncePerRequestFilter {

    private static final List<String> SKIPPED_PREFIXES =
            List.of("/swagger-ui", "/v3/api-docs", "/actuator", "/favicon");

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // A dot in the last path segment means a static file, e.g. /app.js.
        boolean staticFile = path.substring(path.lastIndexOf('/') + 1).contains(".");
        return staticFile || SKIPPED_PREFIXES.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!log.isDebugEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }
        String query = request.getQueryString();
        String target = query == null ? request.getRequestURI() : request.getRequestURI() + "?" + query;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean loggedIn = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
        String user = loggedIn ? auth.getName() : "anonymous";
        String role = loggedIn ? roleOf(auth) : "none";
        log.debug("[REQ] {} {}  user={} ({})", request.getMethod(), target, user, role);

        long start = System.nanoTime();
        try {
            filterChain.doFilter(request, response);
        } finally {
            // Read after the chain, so 401/403 written by security handlers show up.
            long ms = (System.nanoTime() - start) / 1_000_000;
            log.debug("[RES] {}  {} ms", response.getStatus(), ms);
        }
    }

    private static String roleOf(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(authority -> authority.getAuthority().replaceFirst("^ROLE_", ""))
                .findFirst()
                .orElse("none");
    }
}
