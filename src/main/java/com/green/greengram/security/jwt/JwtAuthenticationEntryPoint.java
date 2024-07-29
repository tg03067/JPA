package com.green.greengram.security.jwt;

import com.green.greengram.exception.CustomException;
import com.green.greengram.exception.MemberErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

// 잘못된 토큰, 만료된 토큰, 지원하지 않는 토큰 처리
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver ;

    public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver ;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // response.sendError(HttpServletResponse.SC_UNAUTHORIZED); // 401 에러 리턴
        resolver.resolveException(request, response, null, new CustomException(MemberErrorCode.UNAUTHENTICATED)) ;
    }
}
