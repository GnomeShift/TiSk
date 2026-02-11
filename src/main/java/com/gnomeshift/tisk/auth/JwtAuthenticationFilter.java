package com.gnomeshift.tisk.auth;

import com.gnomeshift.tisk.user.UserRepository;
import com.gnomeshift.tisk.user.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Skip if header is missing
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        try {
            final UUID userId = UUID.fromString(jwtService.extractId(jwt));

            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                // Check for refresh token instead of access
                if (!jwtService.isAccessToken(jwt)) {
                    throw new MalformedJwtException("Refresh token can't be used for authentication");
                }

                // Get user from DB
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

                // Check status
                if (!user.isAccountNonLocked()) {
                    throw new LockedException("User account is locked");
                }

                // Check signature and expiration
                if (jwtService.isTokenValid(jwt, user)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (ExpiredJwtException e) {
            log.warn("JWT token expired: {}", e.getMessage());
            resolver.resolveException(request, response, null, e);
        }
        catch (MalformedJwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            resolver.resolveException(request, response, null, e);
        }
        catch (EntityNotFoundException e) {
            log.warn("User not found: {}", e.getMessage());
            resolver.resolveException(request, response, null, e);
        }
        catch (Exception e) {
            log.error("JWT authentication error: {}", e.getMessage());
            resolver.resolveException(request, response, null, e);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/auth/register")
                || path.equals("/api/auth/login")
                || path.equals("/api/auth/refresh")
                || path.startsWith("/error");
    }
}
