package com.green.greengram.user;

import com.green.greengram.common.AppProperties;
import com.green.greengram.common.CookieUtils;
import com.green.greengram.common.CustomFileUtils;
import com.green.greengram.security.AuthenticationFacade;
import com.green.greengram.security.SignInProviderType;
import com.green.greengram.security.jwt.JwtTokenProviderV2;
import com.green.greengram.security.MyUser;
import com.green.greengram.security.MyUserDetails;
import com.green.greengram.user.model.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.mindrot.jbcrypt.BCrypt.gensalt;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserMapper mapper;
    private final CustomFileUtils customFileUtils;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProviderV2 jwtTokenProvider;
    private final CookieUtils cookieUtils;
    private final AuthenticationFacade authenticationFacade;
    private final AppProperties appProperties;
    // SecurityContextHolder > Context > Authentication(UserNamePasswordAuthenticationToken)
    //    > MyUserDetails > MyUser

    @Override @Transactional
    public int postUser(MultipartFile pic, SignUpPostReq p){
        String saveFileName = customFileUtils.makeRandomFileName(pic);
        p.setPic(saveFileName);
        String password = passwordEncoder.encode(p.getUpw());
        //String hashPassword = BCrypt.hashpw(p.getUpw(), BCrypt.gensalt());
        p.setUpw(password);

        int result = mapper.postUser(p);
        if(pic == null){
            return result ;
        }
        try {
            String path = String.format("user/%d", p.getUserId());
            customFileUtils.makeFolders(path);
            String target = String.format("%s/%s",path,saveFileName);
            customFileUtils.transferTo(pic,target);
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("파일오류");
        }
        return result ;
    }
    @Override
    public SignInRes getUserById(SignInPostReq p, HttpServletResponse res){
        p.setProviderType(SignInProviderType.LOCAL.name());
        User user = mapper.signInPost(p);
        if(Objects.isNull(user)){
            throw new RuntimeException("아이디를 확인해 주세요.");
        } else if(!BCrypt.checkpw(p.getUpw(), user.getUpw())){
            throw new RuntimeException("비밀번호를 확인해 주세요");
        }

        MyUser myUser = MyUser.builder().
                userId(user.getUserId()).
                role("ROLE_USER").
                build();
        /*
        access, refresh token 에 myUser ( 유저 Pk, 권한정보 ) 를 담는다.
        refresh token 에 myUser 정보를 넣는 이유는 access token 을 재발급 받을 때,
        access token 에 myUser 를 담기 위해서 담는다.
        왜 담았냐. FE가 accessToken 이 계속 backEnd 로 요청을 보낼 때,
                  Header 에 넣어서 보내준다
        요청이 올 때마다 Request 에 token 이 담겨져 있는지 체크 ( Filter 에서 한다. )
        token 에 담겨져 있는 muUser 를 빼내서 사용하기 위해 myUser 를 담았다.
         */
        String accessToken = jwtTokenProvider.generateAccessToken(myUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(myUser);
        // refreshToken 은 보안 쿠키를 이용해서 처리 ( FE가 따로 작업을 하지 않아도 아래 cookie 항상 넘어온다. )
        int refreshTokenMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
        cookieUtils.deleteCookie(res, "refresh-token");
        cookieUtils.setCookie(res, "refresh-token", refreshToken, refreshTokenMaxAge);

        return SignInRes.builder().
                userId(user.getUserId()).
                nm(user.getNm()).
                pic(user.getPic()).
                accessToken(accessToken).
                build();
    }
    @Override
    public UserInfoGetRes getUserInfo(UserInfoGetReq p){
        p.setSignedUserId(authenticationFacade.getLoginUserId());
        return mapper.selProfileUserInfo(p);
    }
    @Override @Transactional
    public String patchProfilePic(UserProfilePatchReq p){
        p.setSignedUserId(authenticationFacade.getLoginUserId());
        String fileNm = customFileUtils.makeRandomFileName(p.getPic());
        p.setPicName(fileNm);
        mapper.updProfilePic(p); //DB수정

        //기존 폴더 삭제
        try {
            String midPath = String.format("user/%d",p.getSignedUserId());
            String delAbsoluteFolderPath = String.format("%s%s", customFileUtils.uploadPath, midPath);
            customFileUtils.deleteFolder(delAbsoluteFolderPath);

            customFileUtils.makeFolders(midPath);
            String filePath = String.format("%s/%s", midPath, fileNm);
            customFileUtils.transferTo(p.getPic(), filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return fileNm;
    }
    @Override
    public Map<String, String> getAccessToken(HttpServletRequest req){
        Cookie cookie = cookieUtils.getCookie(req, "refresh-token");
        if(cookie == null){ // refresh-token 값이 쿠키에 존재여부
            throw new RuntimeException();
        }
        String refreshToken = cookie.getValue();
        if(!jwtTokenProvider.isValidateToken(refreshToken)){ //refresh token 만료시간 체크
            throw new RuntimeException();
        }
        UserDetails auth = jwtTokenProvider.getUserDetailsFromToken(refreshToken);
        MyUser myUser = ((MyUserDetails)auth).getUser();
        String accessToken = jwtTokenProvider.generateAccessToken(myUser);

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", accessToken);
        return map;
    }
}
