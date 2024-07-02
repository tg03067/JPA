package com.green.greengram.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        String redirectUriAfterLogin = request.getParameter(appProperties.getOauth2().getRedirectUriParamCookieName()) ;
        log.info("redirectUriAfterLogin: {}", redirectUriAfterLogin) ;
        if(StringUtils.isNoneBlank(redirectUriAfterLogin)){
            cookieUtils.setCookie(response
                    , appProperties.getOauth2().getAuthorizationRequestCookieName()
                    , redirectUriAfterLogin
                    , appProperties.getOauth2().getCookieExpirySecond()) ;
        }
    }
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        log.info("CookieRepository -removeAuthorizationRequest") ;
        return this.loadAuthorizationRequest(request) ;
    }
    public void removeAuthorizationRequestCookies(HttpServletResponse response) {
        log.info("CookieRepository -removeAuthorizationRequestCookies") ;
        cookieUtils.deleteCookie(response, appProperties.getOauth2().getAuthorizationRequestCookieName()) ;
        cookieUtils.deleteCookie(response, appProperties.getOauth2().getRedirectUriParamCookieName()) ;
    }
}
