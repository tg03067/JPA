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

/*
    < 프론트엔드 redirect_uri 가 허용헌 uri 인지 체크하는 부분의 현 상태 >
    인증받고 싶은 소셜로그인 선택 ( redirect_uri 는 소셜로그인 마무리되고 프론트엔드로 가는 url )
    소셜로그인 화면 출력
    아이디/비번 입력해서 로그인 시도 ( redirect_uri 는 백엔드로 가는 url )
    인가코드 받기 위한 작업이 이루어짐.
    제공자 ( provider ) 는 아이디/비번이 일치한다면 백엔드 redirect_uri로 인가코드 보내준다.
    백엔드는 인가코드로 access_token 을 받기 위한 작업이 이루어짐
    백엔드는 access_token 으로 사용자 벙보를 받기 위한 작업이 이루어짐
    로컬 로그인 작업 수행 ( SuccessHandler ) ( 프론트엔드 redirect_uri 가 허용헌 uri 인지 체크 )
    프론트엔드 redirect_uri 로 리다이렉트를 하면서 필요한 정보를 파라미터로 보내준다.

    인증받고 싶은 소셜로그인 선택시 프론트엔드 redirect_uri 가 허용한 uri 인지 체크를 하도록 변경, 필터로 해결

 */

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
                .roles(myUserOAuth2Vo.getRoles())
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

}
