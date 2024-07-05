package com.green.greengram.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengram.common.AppProperties;
import com.green.greengram.security.MyUser;
import com.green.greengram.security.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
/*
JWT 생성, Request(요청)의 Header 에서 token 얻기, 확인 ( validate : 토큰 변질이 없었나, 만료시간이 지났나? ), Claim(data) 넣고 빼기
 */
@Slf4j
@Component
public class JwtTokenProviderV2 {
    private final ObjectMapper om;
    private final AppProperties appProperties;
    private final SecretKey secretKey;

    public JwtTokenProviderV2(ObjectMapper om, AppProperties appProperties) {
        this.om = om;
        this.appProperties = appProperties;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(appProperties.getJwt().getSecret()));
        // 암호화할때 사용하는 키 만드는 기법
        // 암호화, 복호화할 때 사용하는 키를 생성하는 부분, decode 메소드에 보내는 아규먼트값은 우리가 설정한 문자열
    }
    public String generateAccessToken(MyUser myUser) {
        return generateToken(myUser, appProperties.getJwt().getAccessTokenExpiry());
        // yaml 파일에서 app.jw.access-token-expiry 내용을 가져오는 부분
    }
    public String generateRefreshToken(MyUser myUser) {
        return generateToken(myUser, appProperties.getJwt().getRefreshTokenExpiry());
        // yaml 파일에서 app.jw.refresh-token-expiry 내용을 가져오는 부분
    }
    private String generateToken(MyUser myUser, long tokenValidMilliSecond) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis())) // JWT 생성일시
                .expiration(new Date(System.currentTimeMillis() + tokenValidMilliSecond)) // JWT 만료일시
                .claims(createClaims(myUser)) // claims 는 payload 에 저장하고 싶은 내용을 저장
                .signWith(secretKey, Jwts.SIG.HS512)  // 서명 ( JWT 암호화 선택, 위변조 검증 )
                .compact();
                // 토큰 생성

        // .메소드호출.메소드호출.메소드호출
        //          >> 체이닝 기법, 원리는 메소드 호출 시 자신의 주소값 리턴을 하기 때문.
    }
    private Claims createClaims(MyUser myUser) {
        // JWT body 에 담는 내용
        try {
            String json = om.writeValueAsString(myUser); // 객채 to JSON
            return Jwts.claims().add("signedUser", json).build(); // Claims 에 JSON 저장
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public Claims getAllClaims(String token) {
        // token : 암호화 된 부분을 받아서 Payload 부분을 뺴내겠다/
        return Jwts
                .parser()
                .verifyWith(secretKey) // 똑같은 키로 복호화
                .build()
                .parseSignedClaims(token)
                .getPayload(); // JWT 안에 들어있는 payload ( Claims ) 를 리턴
    }
    public UserDetails getUserDetailsFromToken(String token) {
        try {
            Claims claims = getAllClaims(token); // JWT(인증코드) 에 저장되어 있는 Claims 를 얻어온다.
            String json = (String)claims.get("signedUser"); // Claims 에 저장되어 있는 값을 얻어온다. ( 그것이 JSON (Data))
            MyUser user = om.readValue(json, MyUser.class); // JSON > 객체로 변환 (그것이 UserDetails , 정확히는 MyUserDetails )
            MyUserDetails userDetails = new MyUserDetails();
            userDetails.setUser(user);
            return userDetails;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Authentication getAuthentication(String token) {
        //SpringContextHolder 에 저장할 자료를 세팅 ( 나중에 Service 단에서 뻬서 쓸 값,
        // 로그인 처리, 인가 처리를 위해서 )
        UserDetails userDetails = getUserDetailsFromToken(token);
        return userDetails == null ? null
                // JWT Token 에 저장공간이 하나 할당되기를 원함.
                // userDetails = 로그인한 사용자 pk값을 빼내기 위해서
                : new UsernamePasswordAuthenticationToken(userDetails ,
                null ,userDetails.getAuthorities()
                // 권한(인가) 확인 부분
        );
        // UserNamePasswordAuthenticationToken 객체를 SpringContextHolder 에 저장하는 자체만으로도 인증완료
        // userDetails 는 로그인한 사용자의 정보를 controller or service 단에서 뺴서 사용하기 위함.
        // userDetails.getAuthorities() 는 인가(권한)부분 세팅, 현재는 권한은 하나만 가질 수 있음,
        // 다수 권한은 못가지게 세팅해 놨음.
    }
    public boolean isValidateToken(String token) {
        try {
            // (original) 만료시간이 안 지났으면 리턴 false, 지났으면 리턴 true

            return !getAllClaims(token).getExpiration().before(new Date());
            // Date() 가 빈칸이면 현재시간이 들어감.
            // (변환) 만료시간이 안 지났으면 리턴 true, 지났으면 리턴 false
        } catch (Exception e){
            return false;
        }
    }
    public String resolveToken(HttpServletRequest req) {
        // 요청이 오면 JWT 를 열어보는 부분 > 헤더에서 토큰(JWT) 을 꺼낸다.
        // FE가 BE 요청을 보낼 때 ( 로그인을 했다면 ) 항상 JWT 를 보낼건데 Header 에
        // 서로 약속한 Key 에 저장해서 보낸다.
        String jwt = req.getHeader(appProperties.getJwt().getHeaderSchemaName());
        // String auth = req.getHeader("authorization"); 이렇게 작성한 것과 같음. Key 값은 변경가능.
        log.debug("JWT from Header: {}", jwt);
        if(jwt == null){ log.debug("JWT is Null"); return null; }
        // 위 if를 지나쳤다면 FE가 Header 에 authorization 키에 데이터를 담아서 보내왔다는 뜻.
        // auth 에는 "Bearer JWT" 문자열이 있을 것이다. 문자열이 'Bearer' 로 시작하는지 체크

        // if(auth.startsWith("Bearer)
        // { auth 에 저장되어있는 문자열이 "Bearer"로 시작한다면 true, 아니면 false
        // FE와 약속을 만들어야 함.
        // authorization : Bearer JWT 문자열
        if(!jwt.startsWith(appProperties.getJwt().getTokenType())) {
            log.debug("JWT does not start with {}", appProperties.getJwt().getTokenType());
            return null;
        }

        // trim 은 앞뒤에 빈칸제거 ( 중간은 X )
        // 순수한 JWT 문자열만 뽑아내기 위한 문자열 자르기
        // replace(' ', '') 중간에 까지 빈칸은 비지않은 칸으로 바꾸겠다.
        String token = jwt.substring(appProperties.getJwt().getTokenType().length()).trim();
        log.debug("Resolved JWT: {}", token);
        return token;
    }
//public <T> T getData(Class<T> type, HttpServletRequest req, String name){
//    Cookie cookie = getCookie(req, name);
//    if (cookie == null) {
//        return null;
//    }
//    return getCookie(req, name, type);
//}
//
//    public Cookie getCookie(HttpServletRequest req, String name) {
//        // 요청 Header 에 내가 원하는 쿠키를 찾는 메소드
//        Cookie[] cookies = req.getCookies(); // 요청에서 모든 쿠키 정보를 받는다
//        if (cookies != null && cookies.length > 0) { // 쿠키 정보가 있고, 쿠키가 하나 이상 있다면
//            for(Cookie cookie : cookies) {
//                // 찾고자하는 Key 가 있는지 확인 후 있다면 해당 쿠키 리턴
//                if(name.equals(cookie.getName())) {
//                    return cookie;
//                }
//            }
//        }
//        return null;
//    }
//
//    public <T> T getCookie(HttpServletRequest req, String name, Class<T> type) {
//        Cookie cookie = getCookie(req, name);
//        if(cookie == null) {
//            return null;
//        }
//        if(type == String.class) {
//            return (T) cookie.getValue();
//        }
//        return deserialize(cookie, type);
//    }
//
//    public void setCookie(HttpServletResponse res, String name, Object obj, int maxAge) {
//        // value 에 객체를 넣으면 json 형태로 변환해서 cookie 에 저장
//        setCookie(res, name, serialize(obj), maxAge);
//    }
//
//    public void deleteCookie(HttpServletResponse res, String name) {
//        setCookie(res, name, null, 0);
//    }
//
//    public static String serialize(Object obj) {
//        // 객체가 가지고 있는 데이터를 문자열로 변환 ( 암호화 )
//        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(obj));
//    }
//
//    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
//        try {
//            String cookieValue = cookie.getValue();
//
//            // Base64 URL 디코딩 전에 로그 출력
//            System.out.println("Cookie value to decode: " + cookieValue);
//
//            // Base64 URL 인코딩 문자열인지 확인
//            if (!isBase64Encoded(cookieValue)) {
//                throw new IllegalArgumentException("Invalid Base64 encoded string: " + cookieValue);
//            }
//
//            // Base64 URL 디코딩
//            byte[] decodedBytes = Base64.getUrlDecoder().decode(cookieValue);
//
//            // 역직렬화
//            return cls.cast(SerializationUtils.deserialize(decodedBytes));
//        } catch (IllegalArgumentException e) {
//            // 디코딩 중 예외 발생 시 로그 출력
//            System.err.println("Failed to decode Base64 string: " + cookie.getValue());
//            throw e;
//        }
//    }
//
//    // Base64 인코딩된 문자열인지 확인하는 헬퍼 메서드
//    private static boolean isBase64Encoded(String value) {
//        try {
//            Base64.getUrlDecoder().decode(value);
//            return true;
//        } catch (IllegalArgumentException e) {
//            return false;
//        }
//    }
}