package com.green.greengram.user;

import com.green.greengram.common.CustomFileUtils;
import com.green.greengram.user.model.*;
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

        SignInPostReq p3 = new SignInPostReq();
        p3.setUid("id3");
        given(mapper.signInPost(p3.getUid())).willReturn(null);
        Throwable ex1 = assertThrows(RuntimeException.class, () -> service.getUserById(p3), "아이디 없음 예외처리 안함.");
        assertEquals("아이디를 확인해 주세요.", ex1.getMessage(), "아이디 없음, 에러메시지 다름.");

        SignInPostReq p4 = new SignInPostReq();
        p4.setUid("id4");
        p4.setUpw("6666");
        String hashUpw4 = BCrypt.hashpw("7777", BCrypt.gensalt());
        User user4 = new User(10, p4.getUid(), hashUpw4, "길동4", "pic4.jpg", "1111-11-11", null);
        given(mapper.signInPost(p4.getUid())).willReturn(user4);
        Throwable ex2 = assertThrows(RuntimeException.class, () -> service.getUserById(p4), "비밀번호 다름 예외처리 안함.");
        assertEquals("비밀번호를 확인해 주세요", ex2.getMessage(), "비밀번호 다름, 에러메시지 다름.");

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
    void patchProfilePic() throws IOException {
        String picName = "2372b92a-4933-47ec-9431-6489c1cb1239.png";
        MultipartFile file1 =
                new MockMultipartFile("pic", "2372b92a-4933-47ec-9431-6489c1cb1239.png","image/png",
                        new FileInputStream(String.format("D:/download/greengram_tdd/user/2/%s", picName)));
        UserProfilePatchReq p1 = new UserProfilePatchReq();
        p1.setSignedUserId(1);
        p1.setPicName(picName);
        p1.setPic(file1);
        given(mapper.updProfilePic(p1)).willReturn(1);
        File saveFile1 = new File(uploadPath, String.format("%s/%d/%s", "user", p1.getSignedUserId(), p1.getPicName()));
        String result = service.patchProfilePic(p1);

        assertEquals(1, mapper.updProfilePic(p1));
        assertFalse(saveFile1.exists(), "1. 먼가이상");
        assertNotEquals(p1.getPicName(), picName);
    }
}