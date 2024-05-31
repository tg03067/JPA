package com.green.greengram.user;

import com.green.greengram.common.CustomFileUtils;
import com.green.greengram.user.model.SignInPostReq;
import com.green.greengram.user.model.SignInRes;
import com.green.greengram.user.model.SignUpPostReq;
import com.green.greengram.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@TestPropertySource(
        properties = {
                "file.dir=D:/download/greengram_tdd/"
        }
)
@ExtendWith(SpringExtension.class)
@Import({ CustomFileUtils.class, UserServiceImpl.class})
class UserServiceTest {
    @Value("${file.dir}") String uploadPath;
    @MockBean UserMapper mapper;
    @Autowired UserService service;

    @Test
    void postUser() throws IOException {
        String p1Upw = "abc";
        SignUpPostReq p1 = new SignUpPostReq();
        p1.setUserId(100);
        p1.setUpw(p1Upw);

        given(mapper.postUser(p1)).willReturn(1);

        String p2Upw = "def";
        SignUpPostReq p2 = new SignUpPostReq();
        p2.setUserId(200);
        p2.setUpw(p2Upw);

        given(mapper.postUser(p2)).willReturn(2);

        MultipartFile fm1 =
                new MockMultipartFile("pic", "2372b92a-4933-47ec-9431-6489c1cb1239.png","image/png",
                        new FileInputStream("D:/download/greengram_tdd/user/2/2372b92a-4933-47ec-9431-6489c1cb1239.png"));
        int result1 = service.postUser(fm1, p1);
        assertEquals(1, result1);

        File saveFile1 = new File(uploadPath,
                String.format("%s/%d/%s", "user", p1.getUserId(), p1.getPic()));
        assertTrue(saveFile1.exists(), "1. 파일이 만들어지지 않음");
        saveFile1.delete();
        assertNotEquals(p1Upw, p1.getUpw());

        int result2 = service.postUser(fm1, p2);
        assertEquals(2, result2);
        File savedFile2 = new File(uploadPath
                , String.format("%s/%d/%s", "user", p2.getUserId(), p2.getPic()));
        assertTrue(savedFile2.exists(), "2. 파일이 만들어지지 않음");
        savedFile2.delete();
        assertNotEquals(p2Upw, p2.getUpw());
    }

    @Test
    void getUserById() {
        SignInPostReq p1 = new SignInPostReq();
        p1.setUid("id1");
        p1.setUpw("1212");
        String hashUpw1 = BCrypt.hashpw(p1.getUpw(), BCrypt.gensalt());

        SignInPostReq p2 = new SignInPostReq();
        p2.setUid("id2");
        p2.setUpw("2121");
        String hashUpw2 = BCrypt.hashpw(p2.getUpw(), BCrypt.gensalt());

        User user1 = new User(10, p1.getUid(), hashUpw1, "길동1", "pic1.jpg", "1111-11-11", null);
        given(mapper.signInPost(p1.getUid())).willReturn(user1);

        User user2 = new User(20, p2.getUid(), hashUpw2, "길동2", "pic2.jpg", "1111-11-11", null);
        given(mapper.signInPost(p2.getUid())).willReturn(user2);

        try(MockedStatic<BCrypt> mockedStatic = mockStatic(BCrypt.class)) {
            mockedStatic.when(() -> BCrypt.checkpw(p1.getUpw(), user1.getUpw())).thenReturn(true);
            mockedStatic.when(() -> BCrypt.checkpw(p2.getUpw(), user2.getUpw())).thenReturn(true);

            SignInRes res1 = service.getUserById(p1);
            assertEquals(user1.getUserId(), res1.getUserId(), "1. Id먼가이상");
            assertEquals(user1.getNm(), res1.getNm(), "2. Nm먼가이상");
            assertEquals(user1.getPic(), res1.getPic(), "3. Pic먼가이상");
            mockedStatic.verify(() -> BCrypt.checkpw(p1.getUpw(), user1.getUpw()));

            SignInRes res2 = service.getUserById(p2);
            assertEquals(user2.getUserId(), res2.getUserId(), "4. Id먼가이상");
            assertEquals(user2.getNm(), res2.getNm(), "5. Nm먼가이상");
            assertEquals(user2.getPic(), res2.getPic(), "6. Pic먼가이상");
            mockedStatic.verify(() -> BCrypt.checkpw(p2.getUpw(), user2.getUpw()));
        }
//        SignInRes res1 = service.getUserById(p1);
//        assertEquals(user1.getUserId(), res1.getUserId(), "1. Id먼가이상");
//        assertEquals(user1.getNm(), res1.getNm(), "2. Nm먼가이상");
//        assertEquals(user1.getPic(), res1.getPic(), "3. Pic먼가이상");
//        try(MockedStatic<BCrypt> mockedStatic = mockStatic(BCrypt.class)){
//            mockedStatic.when(() -> BCrypt.checkpw(p1.getUpw(), user1.getUpw())).thenReturn(true);
//            mockedStatic.when(() -> BCrypt.checkpw(p2.getUpw(), user2.getUpw())).thenReturn(true);
//            boolean result1 = BCrypt.checkpw(p1.getUpw(), user1.getUpw());
//            boolean result2 = BCrypt.checkpw(p2.getUpw(), user2.getUpw());
//            assertTrue(result1);
//            assertTrue(result2);
//            mockedStatic.verify(() -> BCrypt.checkpw(p1.getUpw(), user1.getUpw()));
//            mockedStatic.verify(() -> BCrypt.checkpw(p2.getUpw(), user2.getUpw()));
//        }
    }

    @Test
    void getUserInfo() {
    }

    @Test
    void patchProfilePic() {
    }
}