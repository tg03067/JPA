package com.green.greengram.security.jwt;

import com.green.greengram.exception.CustomException;
import com.green.greengram.exception.MemberErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

// 권한(인가)이 없는 사람이 접근할 때 처리
@Component
public class JwtAuthenticationAccessDeniedHandler implements AccessDeniedHandler {

    private final HandlerExceptionResolver resolver ;

    public JwtAuthenticationAccessDeniedHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver ;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // response.sendError(HttpServletResponse.SC_FORBIDDEN); // 403에러 리턴
        resolver.resolveException(request, response, null, new CustomException(MemberErrorCode.UNAUTHORIZED)) ;
    }
}
