package com.green.greengram.user;

import com.green.greengram.user.model.*;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    int postUser(MultipartFile pic, SignUpPostReq p);
    SignInRes getUserById(SignInPostReq p);
    UserInfoGetRes getUserInfo(UserInfoGetReq p);
    String patchProfilePic(UserProfilePatchReq p);
}
