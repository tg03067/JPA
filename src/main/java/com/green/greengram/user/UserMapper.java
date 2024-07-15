package com.green.greengram.user;

import com.green.greengram.user.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> selTest(long userId);

    int postUser(SignUpPostReq p);
    // User signInPost(SignInPostReq p);
    List<UserInfo> signInPost(SignInPostReq p) ;
    UserInfoGetRes selProfileUserInfo(UserInfoGetReq p);
    int updProfilePic(UserProfilePatchReq p);
}
