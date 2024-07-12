package com.green.greengram.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

// @ConfigurationProperties : yaml 에 작성되어 있는 데이터를 객체화 시켜주는 애노테이션
@Getter
@ConfigurationProperties(prefix = "app") //prefix 의 app 은 application.yaml 파일의 34Line 의 app
public class AppProperties {
    private final Jwt jwt = new Jwt() ;
    private final Oauth2 oauth2 = new Oauth2() ;
    // class 명 jwt는 application.yaml 의 35Line 의 'jwt' 를 의미
    // static 은 없어도되나 성능상 추가
    // 멤버필드명은 application.yaml 의 app/jwt/* 속성명과 매칭
    // application 에서 '-'는 멤버필드에서 카멜케이스기법과 매칭함
    @Getter @Setter
    public static class Jwt {
        private String secret ;
        private String headerSchemaName ;
        private String tokenType ;
        private long accessTokenExpiry ;
        private String refreshTokenCookieName ;
        private long refreshTokenExpiry ;
        private int refreshTokenCookieMaxAge ;

        public void setRefreshTokenExpiry(long refreshTokenExpiry) {
            this.refreshTokenExpiry = refreshTokenExpiry ;
            this.refreshTokenCookieMaxAge = (int)(refreshTokenExpiry * 0.001) ; // ms > s 변환
        }
    }
    @Getter @Setter
    public static class Oauth2 {
        private String baseUri ;
        private String authorizationRequestCookieName ;
        private String redirectUriParamCookieName ;
        private int cookieExpirySecond ;
        private List<String> authorizedRedirectUris ;
    }
}
