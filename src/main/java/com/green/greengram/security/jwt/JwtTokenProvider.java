package com.green.greengram.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengram.common.AppProperties;
import com.green.greengram.security.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/*
V2 와 다른 점은 SecretKey secretKey 멤버필드의 final 유무가 차이점
ObjectMapper, AppProperties 를 생성자를 통해 DI 받고 있음.
SecretKey 는 생성자 호출 이 후에
SecretKey 는 @PostConstruct 애노태에션을 가지고 있는 init 메소드를 통해 초기화됨.
init 는 메소드이지 생성자가 아니기 때문에 final을 초기화 할 수 없다.
SecretKey 를 final 로 세팅할 수 없음.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final ObjectMapper om;
    private final AppProperties appProperties;
    private SecretKey secretKey;

    @PostConstruct //생성자 호출 이후에 한번 실행하는 메소드 ( 생성자 처럼 사용 가능. )
    public void init() {
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(appProperties.getJwt().getSecret()));
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, appProperties.getJwt().getAccessTokenExpiry());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, appProperties.getJwt().getRefreshTokenExpiry());
    }



    private String generateToken(UserDetails userDetails, long tokenValidMilliSecond) {
        return Jwts.builder()
                .claims(createClaims(userDetails))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenValidMilliSecond))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    public Claims createClaims(UserDetails userDetails) {
        try {
            String json = om.writeValueAsString(userDetails);
            return Jwts.claims().add("signedUser", json).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private UserDetails getUserDetailsFromToken(String token) {
        try {
            Claims claims = getAllClaims(token);
            String json = (String)claims.get("signedUser");
            return om.readValue(json, MyUserDetails.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
