package com.green.greengram.user;

import com.green.greengram.common.CookieUtils;
import com.green.greengram.common.CustomFileUtils;
import com.green.greengram.security.JwtTokenProviderV2;
import com.green.greengram.security.MyUserDetails;
import com.green.greengram.user.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    @Override
    @Transactional
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
    public SignInRes getUserById(SignInPostReq p){
        User user = mapper.signInPost(p.getUid());
        if(Objects.isNull(user)){
            throw new RuntimeException("아이디를 확인해 주세요.");
        } else if(!BCrypt.checkpw(p.getUpw(), user.getUpw())){
            throw new RuntimeException("비밀번호를 확인해 주세요");
        }
        //UserDetails userDetails = new MyUserDetails(user.getUserId(), "ROLE_USER");
        UserDetails details = MyUserDetails.builder()
                .userId(user.getUserId())
                .role("ROLE_USER")
                .build();
        String accessToken = jwtTokenProvider.generateAccessToken(details);
        String refreshToken = jwtTokenProvider.generateRefreshToken(details);
        // refreshToken 은 보안 쿠키를 이용해서 처리

        return SignInRes.builder().
                userId(user.getUserId()).
                nm(user.getNm()).
                pic(user.getPic()).
                accessToken(accessToken).
                build();
    }

    @Override
    public UserInfoGetRes getUserInfo(UserInfoGetReq p){
        return mapper.selProfileUserInfo(p);
    }

    @Override
    @Transactional
    public String patchProfilePic(UserProfilePatchReq p){
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
}
