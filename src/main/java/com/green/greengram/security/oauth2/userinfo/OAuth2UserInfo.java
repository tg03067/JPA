package com.green.greengram.security.oauth2.userinfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public abstract class OAuth2UserInfo { // 규격화
    //             < Key 타입,  Value 타입 >
    protected final Map<String, Object> attributes ;
    // Social 플랫폼에서 받아온 Data(JSON) 을 HashMap 으로 컨버팅하여 내가 직접 DI 해준다.

    public abstract String getId(); // 유니크 값 리턴
    public abstract String getName(); // 이름
    public abstract String getEmail(); // 이메일
    public abstract String getProfilePicUrl(); // 프로필 사진 https://.... or null
}
