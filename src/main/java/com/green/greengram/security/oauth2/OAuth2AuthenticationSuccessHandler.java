package com.green.greengram.security.oauth2;

import com.green.greengram.common.AppProperties;
import com.green.greengram.common.CookieUtils;
import com.green.greengram.security.MyUser;
import com.green.greengram.security.MyUserDetails;
import com.green.greengram.security.MyUserOAuth2Vo;
import com.green.greengram.security.jwt.JwtTokenProviderV2;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final OAuth2AuthenticationRequestBasedOnCookieRepository repository ;
    private final JwtTokenProviderV2 jwtTokenProvider ;
    private final AppProperties appProperties ;
    private final CookieUtils cookieUtils ;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request
                                        , HttpServletResponse response
                                        , Authentication authentication) throws IOException, ServletException {
        if(response.isCommitted()) { // 응답 객체가 만료된 경우 ( 다른 곳에서 응답처리를 한 경우 )
            log.error("onAuthenticationSuccess - 응답이 만료됨.") ;
            return ;
        }
        String targetUrl = determineTargetUrl(request, response, authentication) ; // 리다이렉트 할 Url 얻음.
        log.info("targetUrl: {}", targetUrl) ;
        clearAuthenticationAttributes(request, response) ; // 리다이렉트 전 사용했던 자료 삭제
        getRedirectStrategy().sendRedirect(request, response, targetUrl) ; // 리다이렉트 실행
    }
    @Override
    protected String determineTargetUrl(HttpServletRequest request
                                        , HttpServletResponse response
                                        , Authentication authentication) {
        // FE가 소셜 로그인시 보내준 redirect_uri 값
        String redirectUri = cookieUtils.getCookie(request
                , appProperties.getOauth2().getRedirectUriParamCookieName()
                , String.class) ;
        // ( yaml ) app.oauth.authorized-redirect-uri 리스트에 없는 uri 인 경우
        if(redirectUri != null && !hasAuthorizedRedirectUri(redirectUri)) {
            throw new IllegalArgumentException("인증되지 않은 Redirect URI 입니다.") ;
        }

        // FE가 원하는 redirect_url 값이 저장
        String targetUrl = redirectUri == null ? getDefaultTargetUrl() : redirectUri ;

        // user_id, nm, pic, access_token 을 FE 에게 리턴하기 위해 쿼리스트링 만드는 작업
        // MyOAuth2UserService 에서 보내준 MyUserDetails 를 얻는다
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal() ;

        // MyUserDetail 로부터 MyUserOAuth2Vo 를 얻는다.
        MyUserOAuth2Vo myUserOAuth2Vo = (MyUserOAuth2Vo) myUserDetails.getUser() ;

        // JWT 를 만들기 위해 MyUser 객체화
        MyUser myUser = MyUser.builder()
                .userId(myUserOAuth2Vo.getUserId())
                .role(myUserOAuth2Vo.getRole())
                .build() ;

        String accessToken = jwtTokenProvider.generateAccessToken(myUser) ;
        String refreshToken = jwtTokenProvider.generateRefreshToken(myUser) ;

        int refreshTokenMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge() ;
        cookieUtils.deleteCookie(response, appProperties.getJwt().getRefreshTokenCookieName()) ;
        cookieUtils.setCookie(response, appProperties.getJwt().getRefreshTokenCookieName(), refreshToken, refreshTokenMaxAge) ;

        //  http://localhost:8080/oauth/redirect?user_id=1&nm=홍길동&pic=https://image.jpg&access_token 으로 리턴
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("user_id", myUserOAuth2Vo.getUserId())
                .queryParam("nm", myUserOAuth2Vo.getNm()).encode()
                .queryParam("pic", myUserOAuth2Vo.getPic())
                .queryParam("access_token", accessToken)
                .build()
                .toUriString() ;
    }
    public void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response){
        super.clearAuthenticationAttributes(request) ;
        repository.removeAuthorizationRequestCookies(response) ;
    }
    public boolean hasAuthorizedRedirectUri(String uri) { // 우리가 설정한 redirect-uri 인지 체크
        URI savedCookieRedirectUri = URI.create(uri) ; // uri 객체 좋은 점 : 쿼리스트링 뺴내기 좋음
        log.info("clientRedirectUri.getHost() : {}", savedCookieRedirectUri.getHost()) ; // http://localhost: 까지 날아옴 /** 부분 전까지 날아옴
        log.info("clientRedirectUri.getPort() : {}", savedCookieRedirectUri.getPort()) ; // 포트번호가 날아옴.

        for ( String redirectUri : appProperties.getOauth2().getAuthorizedRedirectUris() ){
            URI authorizedUri = URI.create(redirectUri) ;
            if ( savedCookieRedirectUri.getHost().equalsIgnoreCase(authorizedUri.getHost())
                    && savedCookieRedirectUri.getPort() == authorizedUri.getPort() ){
                return true ;
            }
        }
        return false ;
    }
}
