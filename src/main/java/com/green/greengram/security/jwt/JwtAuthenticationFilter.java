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
        // img, css, js, favicon 등을 요청할 때는 FE가 Header 에 accessToken 을 담지 않았을 때( FE의 실수 or 비로그인 ).
        log.info("JwtAuthenticationFilter-Token: {}", token);

        // 토큰이 정상적으로 저장되어 있고, 만료가 되지 않았다면 >> 로그인 처리
        if(token != null && jwtTokenProvider.isValidateToken(token)) {
            // Security Context Holder 에 인증정보를 넣는다. ( 맥락동안 살아있는 )
            // Context 에 담기 위한 Authentication 객체 생성
            // Token 으로부터 myUser 얻고 > MyUserDetails 에 담고 > UsernamePasswordAuthenticationToken 에 담아서 리턴
            // UsernamePasswordAuthenticationToken 이 Authentication 의 자식 클래스
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            auth.getPrincipal();
            if(auth != null) {
                // SecurityContextHolder.getContext() 에 Authentication 객체 주소값을 담으면 인증(로그인)되었다고 인식
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
        /*
            다음 필터한테 넘기겠다.

            만약 로그인이 필요한 엔드포인트 (url)인데 로그인이 되어있지 않으면
            JwtAuthenticationEntryPoint 에 의해서 401에러가 리턴

            만약 권한이 필요한 엔드포인트 (url)인데 권한이 없으면
            JwtAuthenticationAccessDeniedHandler 에 의해서 403에러가 리턴

            엔드포인트 세팅은 어느 클래스에서 하는가? Configuration 의 FilterChain 메소드에서 한다.
         */
    }
}
