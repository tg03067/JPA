package com.green.greengram.security;

import com.green.greengram.security.jwt.JwtAuthenticationAccessDeniedHandler;
import com.green.greengram.security.jwt.JwtAuthenticationEntryPoint;
import com.green.greengram.security.jwt.JwtAuthenticationFilter;
import com.green.greengram.security.oauth2.MyOAuth2UserService;
import com.green.greengram.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.green.greengram.security.oauth2.OAuth2AuthenticationRequestBasedOnCookieRepository;
import com.green.greengram.security.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    //@Component 로 빈등록을 하였기 때문에 DI가 된다.
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler ;
    private final OAuth2AuthenticationRequestBasedOnCookieRepository repository ;
    private final OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler ;
    private final MyOAuth2UserService myOAuth2UserService ;

    /*
        메소드 빈등록으로 주로 쓰는 케이스(현재기준) : Security 와 관련된 빈등록을 여러개 하고 싶은 때
        메소드 현식으로 빈등록을 하면 한 곳에 모을 수가 있으니 좋다.
        메소드 빈등록을 하지않으면 각각 클래스로 만들어야한다.
     */
    @Bean // 메소드 타입의 빈 등록 ( 파라미터, 리턴타입 중요 )
          // 파라미터는 빈 등록할 때 필요한 객체
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 파라미터없이 내가 직전 new 객체화해서 리턴으로 빈 등록가능
        CommonOAuth2Provider o ;

        /*
       return http.sessionManagement(new Customizer<SessionManagementConfigurer<HttpSecurity>>() {
            @Override
            public void customize(SessionManagementConfigurer<HttpSecurity> session) {
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            }
        }).build();
        new Supplier<>(){
            @Override
            public String get() {
                return null;
            }
        };
        new Function<Integer, Integer>(){
            @Override
            public Integer apply(Integer Integer) {
                return null;
            }
        };
        */
        // seurity에서 세션을 사용하지 않겠다. 존재하지않음을 세팅.
        return http.sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(https -> https.disable())
                // ( SSR 서버사이드 렌더링하지않는다. )
                // 즉, html화면을 백엔드가 만들지 않는다.
                // 백엔드에서 화면을 만들지 않더라도 위 세팅을 끄지않아도 괜찮다.
                // 사용하지 않는 걸 끔으로써 리소스 확보 하기 위해서 사용하는 개념.
                // 정리하면, 시큐리터에서 제공해주는 로그인 화면 사용하지 않겠다.
                .formLogin(form -> form.disable())
                // from 로그인 방식을 사용하지 않음을 세팅.
                .csrf(csrf -> csrf.disable())
                // CSRF 백엔드에서 처리 ( CORS랑 많이 헷갈려 함. 프론트에서의 문제 )
                // Cross-Site Request Forgery 공격 : 사이트 간 요청 위조
                // 로그인 방식이 Session일 때만 문제가 발생할 수 있음.
                // 비정상적인 루트로 요청이 날아가게 만듦.
                // Ex) CRSF 취약정이 보완이 되지 않은 사이트를 찾은 다음,
                //     원하는 요청방법을 분석하여 원하는 요청이 날아가게끔 유도
                /*
                // 만약, permitAll 메소드가 void 였다면 아래와 같이 작성해야함.
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("","").permitAll();
                    auth.requestMatchers("").authenticated();
                })
                //permitAll 메소드가 자기 자신의 주소값을 리턴한다면 체이닝 기법 사용 가능
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("","").permitAll().
                        requestMatchers("").authenticated();
                })
                // 체이닝 기법
                */
                .authorizeHttpRequests(auth ->
                                auth.requestMatchers(
                                        "/api/feed",
                                        "/api/feed/*",
                                        "/api/user/pic",
                                        "/api/user/follow"
                                )
                                .authenticated()
                                .anyRequest().permitAll()
//                                auth.requestMatchers(
//                        // 회원가입, 로그인 인증이 안 되어 있어도 사용가능하게 세팅
//                        "/api/user/sign-up",
//                        "/api/user/sign-in",
//                        "/api/user/access-token",
//                        // swagger 사용할 수 있게 세팅
//                        "/swagger",
//                        "/swagger-ui/**",
//                        "/v3/api-docs/**",
//                        // 사진
//                        "/pic/**",
//                        //프론트 화면 보일수 있게 세팅
//                        "/",
//                        "/index.html",
//                        "/css/**",
//                        "/static/**",
//                        "/js/**",
//                        "/fimg/**",
//                        "/manifest.json",
//                        "/favicon.ico",
//                        "/logo192.png",
//                        // 프론트에서 사용하는 라우터 주소
//                        "/sign-in",
//                        "/sign-up",
//                        "/profile/*",
//                        "/feed" ,
//
//                        //actuator
//                        "/actuator",
//                        "/actuator/**"
//                                ).permitAll()
//                        .anyRequest().authenticated()
                        // 어떤 요청이든 인증이 되어야함.
                        // 로그인이 되어 있어야만 허용.

//                         앞에서 부터 순서가 중요함. anyRequest는 마지막에
//                         단점 : Swagger가 다 막힘.
//                         requestMatchers
//                         권한 세팅, Security의 핵심
//                         로그인이 되지않아도 사용할 수 있음을 세팅.
//                         permitAll : 나머지는 인증이 필요하다. 나 자신의 주소값 호출, 그러므로 { } 를 사용하지않고 작성가능
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                        .accessDeniedHandler(new JwtAuthenticationAccessDeniedHandler()))
                .oauth2Login( oauth2 -> oauth2.authorizationEndpoint(
                        auth -> auth.baseUri("/oauth2/authorization")
                                .authorizationRequestRepository(repository)
                    )
                        .redirectionEndpoint( redirection -> redirection.baseUri("/*/oauth2/code/*"))
                        .userInfoEndpoint(userInfo -> userInfo.userService(myOAuth2UserService))
                        .successHandler(oauth2AuthenticationSuccessHandler)
                        .failureHandler(oauth2AuthenticationFailureHandler)
                )
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 암호화된 암호를 매칭시킬 때 사용 Spring 표준 ( 규격화 )
        // Ex ) 다른 모양의 코드를 돼지코로 만들어주는 변압기의 역할
        return new BCryptPasswordEncoder();
    }
}
