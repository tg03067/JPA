package com.green.greengram.security.oauth2;

import com.green.greengram.security.MyUser;
import com.green.greengram.security.MyUserDetails;
import com.green.greengram.security.MyUserOAuth2Vo;
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

/*
    MyOAuth2UserService :
    OAuth2 제공자 ( 구글, 페이스북, 카카오, 네이버 등 ) 로 부터 Access-Token 받은 후 loadUser 메소드가 호출이 됨.
    ( 스프링 시큐리티에 구현되어 있음. )
    OAuth2 사용자 정보를 가져와서 ( 이미 구현되어 있음. super.loadUser(userRequest); )
    OAuth2User 인터페이스를 구현한 객체( 인증 객체 )를 리턴 ( service 에서 해야할 작업 ) / SuccessHandler 로 가는 객체

    FE  > 플랫폼 소셜로그인 아이콘 클릭 ( 리다이렉트 정보 전달 -- 이 리다이렉트는 로그인 완료 후 다시 돌아온 FE 주소값 )
        > 백엔드에 요청 ( ' 나 무슨 소셜로그인 하고 싶어 ' 에 대한 정보가 전달 )
        > 백엔드는 리다이렉트 ( OAuth2 제공자 로그인 화면 )
        > 해당 플랫폼에 아이디/비번을 작성 후 로그인 처리
        > 제공자는 인가 코드를 백엔드에게 보내준다.
        > ( 해당 ) 백엔드는 인가 코드를 가지고 access-token 을 발급받는다.
        > ( 해당 ) access-token 으로 사용자 정보 ( scope 에 작성한 내용 ) 를 받는다.
        > 이후는 자체 로그인 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MyOAuth2UserService extends DefaultOAuth2UserService {
    private final UserMapper mapper ;
    private final OAuth2UserInfoFactory oAuth2UserInfoFactory ;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth2 사용자 정보를 가져오는 메소드
        try {
            return this.process(userRequest) ;
        } catch (Exception e){
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause()) ;
        }
    }
    private OAuth2User process(OAuth2UserRequest userRequest){
        // userRequest 에 access-token 이 이미 있음.
        // JSON 타입으로 넘어온 값이 HashMap 으로 바뀐다.
        OAuth2User oAuth2User = super.loadUser(userRequest) ; // 제공자로부터 사용자정보를 얻음
        // 각 소셜플랫폼에 맞는 enum 타입을 얻는다.
        SignInProviderType signInProviderType = SignInProviderType
                                                    .valueOf(userRequest
                                                    .getClientRegistration() // 사이트마다 google, naver ... 등등에서 던지는 값들이 들어있음
                                                    .getRegistrationId() // 소문자로 문자열이 날아옴 google 내용부분만 날아옴
                                                    .toUpperCase()) ; // 대문자로 바꿔줌.
        // valueOf("GOOGLE") 이런식으로 넘어옴
        // 규격화된 UserInfo 객체로 변환
        // OAuth2User.getAttributes() > Data 가 HashMap 객체로 변환
        OAuth2UserInfo oAuth2UserInfo = oAuth2UserInfoFactory.getOAuth2UserInfo(signInProviderType, oAuth2User.getAttributes()) ;

        // 기존에 회원가입이 되어있는가 체크
        SignInPostReq signInParam = new SignInPostReq() ;
        signInParam.setUid(oAuth2UserInfo.getId()) ; // 플랫폼에서 넘어오는 유니크값 ( 항상 같은 값이며 다른 사용자와 구별되는 유니크 값 )
        signInParam.setProviderType(signInProviderType.name()) ;
        User user = mapper.signInPost(signInParam) ;

        if(user == null){ // 회원가입 처리
            SignUpPostReq signUpParam = new SignUpPostReq() ;
            signUpParam.setProviderType(signInProviderType) ;
            signUpParam.setUid(oAuth2UserInfo.getId()) ;
            signUpParam.setNm(oAuth2UserInfo.getName()) ;
            signUpParam.setPic(oAuth2UserInfo.getProfilePicUrl()) ;
            int result = mapper.postUser(signUpParam) ;
            user = new User(signUpParam.getUserId()
                        , signUpParam.getUid() // or null
                        , signUpParam.getUpw() // or null
                        , signUpParam.getNm()
                        , signUpParam.getPic()
                        , null
                        , null ) ;
        } else { // 이미 회원가입 되어 있었음
            if(user.getPic() == null || ( user.getPic().startsWith("http") && !user.getPic().equals(oAuth2UserInfo.getProfilePicUrl()))){ // 프로필 값이 변경이 되었다면
                // 프로필 사진 변경처리 ( update )
            }
        }

        MyUserOAuth2Vo myUserOAuth2Vo =
                new MyUserOAuth2Vo(user.getUserId(), "ROLE_USER", user.getNm(), user.getPic()) ;

        MyUserDetails signInUser = new MyUserDetails() ;
        signInUser.setUser(myUserOAuth2Vo) ;
        return signInUser ; // authentication 에 저장됨.
    }
}
