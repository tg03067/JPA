package com.green.greengram.user;

import com.green.greengram.user.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserService {
    int postUser(MultipartFile pic, SignUpPostReq p);
    SignInRes getUserById(SignInPostReq p, HttpServletResponse res);
    UserInfoGetRes getUserInfo(UserInfoGetReq p);
    String patchProfilePic(UserProfilePatchReq p);
    Map getAccessToken(HttpServletRequest req);
}
