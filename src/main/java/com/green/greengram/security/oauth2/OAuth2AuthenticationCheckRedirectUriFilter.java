package com.green.greengram.security.oauth2;

import com.green.greengram.common.AppProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationCheckRedirectUriFilter extends OncePerRequestFilter {
    private final AppProperties appProperties ;
    @Override
    protected void doFilterInternal(HttpServletRequest request // 요청에 대한 모든 정보
                                    , HttpServletResponse response // 응답할 수 있는 객체
                                    , FilterChain filterChain // 다음 필터로 req, res 를 전달할 때 사용
    ) throws ServletException, IOException {
        try {
        /*
            호스트값 제외한 uri 를 리턴
            예 ) http://localhost:8080/aaa/bbb
            "/aaa/bbb"를 리턴
         */
            String requestUri = request.getRequestURI();
            log.info("Request URI: {}", requestUri);

            if (requestUri.startsWith(appProperties.getOauth2().getBaseUri())) {
                String redirectUriParam = request.getParameter("redirect_uri");
                if (redirectUriParam != null && !hasAuthorizedRedirectUri(redirectUriParam)) { // 허용하지 않은 URI 라면
                    String errRedirectUrl = UriComponentsBuilder.fromUriString("/err_message")
                            .queryParam("message", "유효한 Redirect URL이 아닙니다.").encode()
                            .toUriString();
                    //  "/err_message?message=유효한 Redirect URL이 아닙니다." ;
                    response.sendRedirect(errRedirectUrl);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            filterChain.doFilter(request, response);
        }
    }
    public boolean hasAuthorizedRedirectUri(String uri) { // 우리가 설정한 redirect-uri 인지 체크
        URI cliectRedirectUri = URI.create(uri) ; // uri 객체 좋은 점 : 쿼리스트링 뺴내기 좋음
        log.info("clientRedirectUri.getHost() : {}", cliectRedirectUri.getHost()) ; // http://localhost: 까지 날아옴 /** 부분 전까지 날아옴
        log.info("clientRedirectUri.getPort() : {}", cliectRedirectUri.getPort()) ; // 포트번호가 날아옴.

        for ( String redirectUri : appProperties.getOauth2().getAuthorizedRedirectUris() ){
            URI authorizedUri = URI.create(redirectUri) ;
            if ( cliectRedirectUri.getHost().equalsIgnoreCase(authorizedUri.getHost())
                    && cliectRedirectUri.getPort() == authorizedUri.getPort()
                    && cliectRedirectUri.getPath().equals(authorizedUri.getPath() )
            ){
                return true ;
            }
        }
        return false ;
    }
}
