package com.green.greengram.security.oauth2;

import com.green.greengram.common.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFilterHandler extends SimpleUrlAuthenticationFailureHandler {
    private final CookieUtils cookieUtils ;
    private final OAuth2AuthenticationRequestBasedOnCookieRepository repository ;
}
