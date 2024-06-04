package com.green.greengram.user;

import com.green.greengram.common.CustomFileUtils;
import com.green.greengram.user.model.UserProfilePatchReq;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@TestPropertySource(
    properties = {
            "file.dir=D:/download/greengram_tdd/"
    }
)
@ExtendWith(SpringExtension.class)
@Import({ UserServiceImpl.class})
public class UserService2Test {
    @Value("${file.dir}") String uploadPath ;
    @MockBean UserMapper mapper ;
    @MockBean CustomFileUtils customFileUtils ;
    @Autowired UserServiceImpl service ;

    @Test
    public void patchProfilePic() throws Exception {
        long signedUserId1 = 500;
        UserProfilePatchReq p1 = new UserProfilePatchReq();
        p1.setSignedUserId(signedUserId1);
        MultipartFile fm1 = new MockMultipartFile("pic", "b.png","image/png",
                new FileInputStream(String.format("%stest/b.png", uploadPath)));
        p1.setPic(fm1);
        String checkFileName = "a1b2.jpg";
        given(customFileUtils.makeRandomFileName(fm1)).willReturn(checkFileName);

        String fileName1 = service.patchProfilePic(p1);
        assertEquals(checkFileName, fileName1);
        verify(mapper).updProfilePic(p1);
        String midPath = String.format("user/%d", p1.getSignedUserId());
        String delFolderPath1 = String.format("%s%s", customFileUtils.uploadPath, midPath);
        verify(customFileUtils).deleteFolder(delFolderPath1);
        verify(customFileUtils).makeFolders(midPath);
        String filePath1 = String.format("%s/%s", midPath, fileName1);
        verify(customFileUtils).transferTo(p1.getPic(), filePath1);
    }

    @Test
    public void patchProfilePic2() throws Exception {
        long signedUserId1 = 600;
        UserProfilePatchReq p1 = new UserProfilePatchReq();
        p1.setSignedUserId(signedUserId1);
        MultipartFile fm1 = new MockMultipartFile("pic", "b.png","image/png",
                new FileInputStream(String.format("%stest/b.png", uploadPath)));
        p1.setPic(fm1);
        String checkFileName = "aaaa.jpg";
        given(customFileUtils.makeRandomFileName(fm1)).willReturn(checkFileName);

        String fileName1 = service.patchProfilePic(p1);
        assertEquals(checkFileName, fileName1);
        verify(mapper).updProfilePic(p1);
        String midPath = String.format("user/%d", p1.getSignedUserId());
        String delFolderPath1 = String.format("%s%s", customFileUtils.uploadPath, midPath);
        verify(customFileUtils).deleteFolder(delFolderPath1);
        verify(customFileUtils).makeFolders(midPath);
        String filePath1 = String.format("%s/%s", midPath, fileName1);
        verify(customFileUtils).transferTo(p1.getPic(), filePath1);
    }
}
