package com.green.greengram.security.jwt;

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
    // 한 페이지에서 요청이 여러번 와도 한번만 filter 만들겠다는 의미 >> OncePerRequestFilter
    private final JwtTokenProviderV2 jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Header 에 authorization 키에 저장되어있는 값을 리턴 ( 있으면 문자열 (JWT), 없으면 null )
        // JWT 값이 있으면 로그인 상태, null 이면 비로그인 상태 (로그아웃 상태)
        String token = jwtTokenProvider.resolveToken(request);
        log.info("JwtAuthenticationFilter-Token: {}", token);

        // 토큰이 정상적으로 저장되어 있고, 만료가 되지 않았다면 >> 로그인 처리
        if(token != null && jwtTokenProvider.isValidateToken(token)) {
            // Security Context Holder 에 인증정보를 넣는다. ( 맥락동안 살아있는 )
            // Context 에 담기 위한 Authentication 객체 생성
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            if(auth != null) {
                // Authentication 객체 주소값을 담으면 인증되었다고 인식
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
        // 다음 필터한테 넘기겠다.
    }
}
