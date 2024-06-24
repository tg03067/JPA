package com.green.greengram.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // 한 페이지에서 요청이 여러번 와도 한번만 filter만들겠다는 의미 >> OncePerRequestFilter
    private final JwtTokenProviderV2 jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);
        log.info("JwtAuthenticationFilter-Token: {}", token);

        // 토큰이 정상적으로 저장되어 있고, 만료가 되지 않았다면
        if(token != null && jwtTokenProvider.isValidateToken(token)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            if(auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }
}
