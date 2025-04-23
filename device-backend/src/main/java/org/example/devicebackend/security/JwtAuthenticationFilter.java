package org.example.devicebackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Arrays.stream(cookies)
                    .filter(cookie -> "token".equals(cookie.getName()))
                    .findFirst()
                    .ifPresent(cookie -> {
                        String token = cookie.getValue();
                        Map<String, Object> claims = jwtUtil.validateTokenAndGetClaims(token);
                        String username = (String) claims.get("sub");
                        boolean isAdmin = (boolean) claims.get("isAdmin");

                        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                            List<SimpleGrantedAuthority> authorities = isAdmin
                                    ? List.of(new SimpleGrantedAuthority("ADMIN"))
                                    : List.of(new SimpleGrantedAuthority("USER"));

                            UsernamePasswordAuthenticationToken authenticationToken =
                                    new UsernamePasswordAuthenticationToken(username, null, authorities);

                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }
                    });
        }
        filterChain.doFilter(request, response);
    }
}
