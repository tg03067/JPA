package com.green.greengram.security.oauth2;

import com.green.greengram.common.AppProperties;
import com.green.greengram.common.CookieUtils;
import jakarta.servlet.http.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;


//oauth2_auth_request
/*
    OAuth2 인증 과정 중에 쿠키에 저장하는 데이터는 보안 때문. CSRF 공격을 방지하기 위해 state 값을 사용 .
    OAuth2AuthorizationRequest 를 인가코드 ( 인증코드 ) 받을 때까지 사용
     > access token 받은 이후에는 다시 사용할 가치가 없기때문에 세션에서 삭제.

     만약 터큰이 만려가 되어 퀀한부여 요청을 ( 인증 / 인가 코드 ) 다시 하는 경우 이전에 세션이 존재한다면 현재를 사용하는 것이 아니라
     예전꺼를 사용하기 때문에 문제될 가능성이 있음. 그래서 세션에서 삭제를 한다.
     인가/인증 코드가 1회 용인 것처럼 OAuth2AuthorizationRequest 객체도 1회 용으로 사용.
     ( 인가/인증 코드는 요청 보낼 때마다 값이 달라진다. )

     ( 사용자 정보 받았다 / 못 받았다 분기 )
        성공 > OAuth2AuthenticationSuccessHandler - onAuthenticationSuccess 호출
        실패 > OAuth2AuthenticationFailureHandler -

     스프링 시큐리티 OAuth 처리 때 사용하는 필터가 2개가 있음.
     OAuth2AuthorizationRequestRedirectFilter( AS 가필터 ), OAuth2LoginAuthenticationFilter( AS 나필터 )

     OAuth2AuthorizationRequest( AS a ) 는 소셜로그인 요청할 때마다 생성되는 객체
     1단계 인가코드(임시코드, 인증코드)를 요청할 때 a를 사용함
     2단계 Access Token 을 요청한 이후에는 A를 사용할 일이 발생하지 않기 때문에 Cookie 에서 삭제

     세션을 이용해서 처리하는 방식은 확장이 불리함. >> 쿠키로 해결.
        > 그래서 이전에 세션에서 삭제 처리를 removeAuthorizationRequest 메소드에서 했던거 같음.

    가필터에서 removeAuthorizationRequest 메소드를 호출해서 리턴받은 값을 활용한다.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationRequestBasedOnCookieRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    private final CookieUtils cookieUtils ;
    private final AppProperties appProperties ;
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request){
        log.info("CookieRepository - loadAuthorizationRequest") ;
        return cookieUtils.getCookie( request
                , appProperties.getOauth2().getAuthorizationRequestCookieName()
                , OAuth2AuthorizationRequest.class ) ;
    }
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        log.info("CookieRepository - saveAuthorizationRequest") ;
        if ( authorizationRequest == null ) { //
            this.removeAuthorizationRequestCookies(response) ;
            return ;
        }
        cookieUtils.setCookie(response
                , appProperties.getOauth2().getAuthorizationRequestCookieName()
                , authorizationRequest
                , appProperties.getOauth2().getCookieExpirySecond()) ;

        // FE로 돌아갈 redirect 주소값 ( 즉, FE가 redirect_uri 파라미터로 백엔드에 보내준 값 )
        String redirectUriAfterLogin = request.getParameter(appProperties.getOauth2().getRedirectUriParamCookieName()) ;
        log.info("redirectUriAfterLogin: {}", redirectUriAfterLogin) ;
        if(StringUtils.isNoneBlank(redirectUriAfterLogin)){
            cookieUtils.setCookie(response
                    //, appProperties.getOauth2().getAuthorizationRequestCookieName()
                    , appProperties.getOauth2().getRedirectUriParamCookieName()
                    , redirectUriAfterLogin
                    , appProperties.getOauth2().getCookieExpirySecond()) ;
        }
    }
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        log.info("CookieRepository -removeAuthorizationRequest") ;
        return this.loadAuthorizationRequest(request) ; // null
    }
    public void removeAuthorizationRequestCookies(HttpServletResponse response) {
        log.info("CookieRepository -removeAuthorizationRequestCookies") ;
        cookieUtils.deleteCookie(response, appProperties.getOauth2().getAuthorizationRequestCookieName()) ;
        cookieUtils.deleteCookie(response, appProperties.getOauth2().getRedirectUriParamCookieName()) ;
    }
}
