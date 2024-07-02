package com.green.greengram.security.oauth2;

import com.green.greengram.security.MyUser;
import com.green.greengram.security.MyUserDetails;
import com.green.greengram.security.SignInProviderType;
import com.green.greengram.security.oauth2.userinfo.OAuth2UserInfo;
import com.green.greengram.security.oauth2.userinfo.OAuth2UserInfoFactory;
import com.green.greengram.user.UserMapper;
import com.green.greengram.user.model.SignInPostReq;
import com.green.greengram.user.model.SignUpPostReq;
import com.green.greengram.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyOAuth2UserService extends DefaultOAuth2UserService {
    private final UserMapper mapper ;
    private final OAuth2UserInfoFactory oAuth2UserInfoFactory ;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            return this.process(userRequest) ;
        } catch (Exception e){
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause()) ;
        }
    }
    private OAuth2User process(OAuth2UserRequest userRequest){
        // JSON 타입으로 넘어온 값이 HashMap 으로 바뀐다.
        OAuth2User oAuth2User = super.loadUser(userRequest) ;
        // 각 소셜플랫폼에 맞는 enum 타입을 얻는다.
        SignInProviderType signInProviderType = SignInProviderType
                                                    .valueOf(userRequest
                                                    .getClientRegistration() // 사이트마다 google, naver ... 등등에서 던지는 값들이 들어있음
                                                    .getRegistrationId() // 소문자로 문자열이 날아옴 google 부분만 날아옴
                                                    .toUpperCase()) ; // 대문자로 바꿔줌.
        // valueOf("GOOGLE") 이런식으로 넘어옴
        // 규격화된 UserInfo 객체로 변환
        OAuth2UserInfo oAuth2UserInfo = oAuth2UserInfoFactory.getOAuth2UserInfo(signInProviderType, oAuth2User.getAttributes()) ;
        SignInPostReq signInParam = new SignInPostReq() ;
        signInParam.setUid(oAuth2UserInfo.getId()) ; // 플랫폼에서 넘어오는 유니크값 ( 항상 같은 값이며 다른 사용자와 구별되는 유니크 값 )
        signInParam.setProviderType(signInProviderType.name()) ;
        User user = mapper.signInPost(signInParam) ;

        MyUser myUser = new MyUser() ;
        myUser.setRole("ROLE_USER") ;

        if(user == null){ // 회원가입 처리
            SignUpPostReq signUpParam = new SignUpPostReq() ;
            signUpParam.setUid(oAuth2UserInfo.getId()) ;
            signUpParam.setNm(oAuth2UserInfo.getName()) ;
            signUpParam.setPic(oAuth2UserInfo.getProfilePicUrl()) ;
            int result = mapper.postUser(signUpParam) ;
            myUser.setUserId(signUpParam.getUserId()) ; // 회원가입 후 유저 PK 담기
        } else {
            myUser.setUserId(user.getUserId()) ; // 이미 회원가입 된 유저 PK 값 담기
        }

        MyUserDetails signInUser = new MyUserDetails() ;
        signInUser.setUser(myUser) ;
        return signInUser ;
    }
}
