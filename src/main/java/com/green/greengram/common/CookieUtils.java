package com.green.greengram.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.stereotype.Component;


import java.io.Serializable;
import java.util.Base64;

@Slf4j
@Component
public class CookieUtils {
    public <T> T getData(T type, HttpServletRequest req, String name){
        Cookie cookie = getCookie(req, name);
        if(cookie != null){
            return null;
        }
        return null;
    }
    public Cookie getCookie(HttpServletRequest req, String name) {
        // 요청 Header 에 내가 원하는 쿠키를 찾는 메소드
        Cookie[] cookies = req.getCookies(); // 요청에서 모든 쿠키 정보를 받는다
        if (cookies != null && cookies.length > 0) { // 쿠키 정보가 있고, 쿠키가 하나 이상 있다면
            for(Cookie cookie : cookies) {
                // 찾고자하는 Key 가 있는지 확인 후 있다면 해당 쿠키 리턴
                if(name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }
    public <T> T getCookie(HttpServletRequest req, String name, Class<T> type) {
        Cookie cookie = getCookie(req, name) ;
        if(cookie == null) { return null ; }
        if(type == String.class) { return (T) cookie.getValue() ; }
        return deserialize(cookie, type) ;
    }
    public void setCookie(HttpServletResponse res, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value) ;
        cookie.setPath("/") ; // Root URL ( 우리 백엔드 모든 요청에 해당하게 세팅 )
        cookie.setHttpOnly(true) ; // 보안 쿠키
        cookie.setMaxAge(maxAge) ; // 만료시간
        res.addCookie(cookie) ;
    }
    public void setCookie(HttpServletResponse res, String name, Serializable obj, int maxAge) {
        // value 에 객체를 넣으면 json 형태로 변환해서 cookie 에 저장
        this.setCookie(res, name, serialize(obj), maxAge) ;
    }
    public void deleteCookie(HttpServletResponse res, String name) {
        setCookie(res, name, null, 0);
    }
//    public String serialize(Object obj) {
//        // 객체가 가지고 있는 데이터를 문자열로 변환 ( 암호화 )
//                                                    // Object > byte[] > String
//        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(obj));
//    }
//    public <T> T deserialize(Cookie cookie, Class<T> cls) { // 복호화
//        return cls.cast(
//            SerializationUtils.deserialize(
//                Base64.getUrlDecoder().decode(cookie.getValue()) // String > byte[] > Object
//            )
//        ) ;
//    }
    public static String serialize(Object obj) {
    // 객체가 가지고 있는 데이터를 문자열로 변환 ( 암호화 )
        return Base64.getUrlEncoder().encodeToString(org.springframework.util.SerializationUtils.serialize(obj));
    }
    // Deserialize method using URL decoding Base64
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        try {
            String cookieValue = cookie.getValue();

            // Base64 URL 디코딩 전에 로그 출력
            System.out.println("Cookie value to decode: " + cookieValue);

            // Base64 URL 인코딩 문자열인지 확인
            if (!isBase64Encoded(cookieValue)) {
                throw new IllegalArgumentException("Invalid Base64 encoded string: " + cookieValue);
            }

            // Base64 URL 디코딩
            byte[] decodedBytes = Base64.getUrlDecoder().decode(cookieValue);

            // 역직렬화
            return cls.cast(SerializationUtils.deserialize(decodedBytes));
        } catch (IllegalArgumentException e) {
            // 디코딩 중 예외 발생 시 로그 출력
            System.err.println("Failed to decode Base64 string: " + cookie.getValue());
            throw e;
        }
    }
    // Base64 인코딩된 문자열인지 확인하는 헬퍼 메서드
    private static boolean isBase64Encoded(String value) {
        try {
            Base64.getUrlDecoder().decode(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
