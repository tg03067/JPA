package com.green.greengram.user;

import com.green.greengram.user.model.User;
import com.green.greengram.user.model.UserInfoGetReq;
import com.green.greengram.user.model.UserInfoGetRes;
import com.green.greengram.user.model.UserProfilePatchReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@MybatisTest
@ActiveProfiles("tdd")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserMapperTest {

    @Autowired
    private UserMapper mapper;

    @Test
    @DisplayName("유저 select one 테스트")
    void signInPost() {
        User user1 = mapper.signInPost("사용자1");
        if(user1 != null) {
            List<User> userList = mapper.selTest(user1.getUserId());
            User user1Comp = userList.get(0);
            assertEquals(user1Comp, user1);

            User user3 = mapper.signInPost("사용자3");
            List<User> userList3 = mapper.selTest(user3.getUserId());
            User user3Comp = userList3.get(0);
            assertEquals(user3Comp, user3);

            User userNo = mapper.signInPost("사용자asdf");
            assertNull(userNo, "1. 먼가이상");
        }
    }

    @Test
    @DisplayName("유저 profile 테스트")
    void selProfileUserInfo() {
        UserInfoGetReq p1 = new UserInfoGetReq(1, 7);
        UserInfoGetRes p2 = mapper.selProfileUserInfo(p1);
        assertEquals(new UserInfoGetRes("홍길동", "70acbe3c-7ecc-4e29-8749-c27cd811e0d4.png",
                "2024-05-08 13:06:19", 7, 3,0, 0, 0),
                p2, "1. 먼가이상");

        UserInfoGetReq p3 = new UserInfoGetReq(11, 12);
        UserInfoGetRes p4 = mapper.selProfileUserInfo(p3);
        assertNull(p4, "2. 먼가이상");

        UserInfoGetReq p5 = new UserInfoGetReq(5, 10);
        UserInfoGetRes p6 = mapper.selProfileUserInfo(p5);
        assertEquals(new UserInfoGetRes("허리케인", "b8cd485c-31fd-49f5-bde4-d529c41c945a.png",
               "2024-05-16 12:48:02", 0, 0,0, 0, 0),
                p6, "3. 먼가이상");

        assertNotEquals(null, p6, "1. 틀림ㅋㅋ");
    }

    @Test
    @DisplayName("유저 update 테스트")
    void updProfilePic() {
        MultipartFile file = new MockMultipartFile("file", "images123",
                "png", "asdf".getBytes());
        List<User> beforeUserList = mapper.selTest(0);
        long modUserId = 1;
        assertEquals(10, beforeUserList.size(), "1. 과연");
        String picName1 = "test.png";
        UserProfilePatchReq p = new UserProfilePatchReq();
        p.setPicName(picName1);
        p.setSignedUserId(modUserId);
        p.setPic(file);
        int res = mapper.updProfilePic(p);
        assertEquals(1, res, "1. 먼가이상");
        List<User> userList = mapper.selTest(modUserId);
        User user1 = userList.get(0);
        assertEquals(picName1, user1.getPic());

        List<User> afterUserList = mapper.selTest(0);

        for(int i = 0; i < beforeUserList.size(); i++){
            User beforeUser = beforeUserList.get(i);
            if(beforeUser.getUserId() == modUserId){
                assertNotEquals(beforeUser, afterUserList.get(i));
                continue ;
            }
            assertEquals(beforeUser, afterUserList.get(i));
        }
    }

    @Test
    @DisplayName("유저 update0 테스트")
    void updProfilePic1() {
        MultipartFile file = new MockMultipartFile("file", "images123",
                "png", "asdf".getBytes());
        List<User> beforeUserList = mapper.selTest(0);

        UserProfilePatchReq p2 = new UserProfilePatchReq();
        p2.setPicName("");
        p2.setSignedUserId(12);
        p2.setPic(file);
        int res2 = mapper.updProfilePic(p2);
        assertEquals(0, res2, "1. 먼가이상");
        List<User> afterUserList = mapper.selTest(1);
        User user1 = afterUserList.get(0);

        for(int i = 0; i < beforeUserList.size(); i++){
            assertEquals(beforeUserList.get(i), afterUserList.get(i));
        }
        assertEquals(1, afterUserList.size(), "먼가1");
    }
}