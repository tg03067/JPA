package com.green.greengram.feed;

import com.green.greengram.common.CustomFileUtils;
import com.green.greengram.feed.model.*;
import com.green.greengram.feedcomment.model.FeedCommentGetRes;
import org.junit.jupiter.api.DisplayName;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestPropertySource(
        properties = {
                "file.dir=D:/download/greengram_tdd/"
        }
)
@ExtendWith(SpringExtension.class)
@Import({ FeedServiceImpl.class })
class FeedServiceTest {
    @Value("${file.dir}") String uploadPath;
    @MockBean FeedMapper mapper;
    @MockBean CustomFileUtils customFileUtils;
    @Autowired FeedService service;

    @Test
    @DisplayName("피드 post 테스트")
    void postFeed() throws Exception {
        FeedPostReq p = new FeedPostReq();
        p.setFeedId(1);

        List<MultipartFile> pics = new ArrayList<>();
        MultipartFile fm1 = new MockMultipartFile("pic", "a.png","image/png",
                new FileInputStream(String.format("%stest/a.png", uploadPath)));
        MultipartFile fm2 = new MockMultipartFile("pic", "b.png","image/png",
                new FileInputStream(String.format("%stest/b.png", uploadPath)));
        pics.add(fm1);
        pics.add(fm2);
        String randomFileName1 = "aaa.png";
        String randomFileName2 = "bbb.png";
        String[] randomFileArr = { randomFileName1, randomFileName2 };
        given(customFileUtils.makeRandomFileName(fm1)).willReturn(randomFileName1);
        given(customFileUtils.makeRandomFileName(fm2)).willReturn(randomFileName2);

        FeedPostRes res = service.postFeed(pics, p);
        verify(mapper).postFeed(p);
        String path = String.format("feed/%d", p.getFeedId());
        verify(customFileUtils).makeFolders(path);
        FeedPicPostDto picDto = FeedPicPostDto.builder().feedId(p.getFeedId()).build();
        for(int i = 0 ; i < pics.size() ; i++) {
            MultipartFile pic = pics.get(i);
            verify(customFileUtils).makeRandomFileName(pic);
            String fileName = randomFileArr[i];
            String filePath = String.format("%s/%s", path, fileName);
            picDto.getFileNames().add(fileName);
            verify(customFileUtils).transferTo(pic, filePath);
        }
        verify(mapper).postFeedPics(picDto);
        FeedPostRes wastRes = FeedPostRes.builder().
                                feedId(picDto.getFeedId()).
                                pics(picDto.getFileNames()).
                                build();

        assertEquals(wastRes, res, "1. 리턴값이 다름.");
    }

    @Test
    @DisplayName("피드 get 테스트")
    void getFeed() throws Exception {
        FeedGetReq p = new FeedGetReq(1,10,1, 2L);
        List<FeedGetRes> list = new ArrayList<>();

        FeedGetRes fgr1 = new FeedGetRes();
        FeedGetRes fgr2 = new FeedGetRes();
        list.add(fgr1);
        list.add(fgr2);
        fgr1.setFeedId(1);
        fgr1.setContents("aaa");
        fgr2.setFeedId(2);
        fgr2.setContents("bbb");

        given(mapper.selFeed(p)).willReturn(list);
        List<String> picsA = new ArrayList<>();
        picsA.add("a1.jpg");
        picsA.add("a2.jpg");

        List<String> picsB = new ArrayList<>();
        picsB.add("b1.jpg");
        picsB.add("b2.jpg");
        picsB.add("b3.jpg");

        given(mapper.selFeedPicsByFeedId(fgr1.getFeedId())).willReturn(picsA);
        given(mapper.selFeedPicsByFeedId(fgr2.getFeedId())).willReturn(picsB);

        List<FeedCommentGetRes> commentA = new ArrayList<>();
        FeedCommentGetRes fcgrA1 = new FeedCommentGetRes(0,"a1", null, 0, null, null);
        FeedCommentGetRes fcgrA2 = new FeedCommentGetRes(0,"a2", null, 0, null, null);
        commentA.add(fcgrA1);
        commentA.add(fcgrA2);

        List<FeedCommentGetRes> commentB = new ArrayList<>();
        FeedCommentGetRes fcgrB1 = new FeedCommentGetRes(0,"b1", null, 0, null, null);
        FeedCommentGetRes fcgrB2 = new FeedCommentGetRes(0,"b2", null, 0, null, null);
        FeedCommentGetRes fcgrB3 = new FeedCommentGetRes(0,"b3", null, 0, null, null);
        FeedCommentGetRes fcgrB4 = new FeedCommentGetRes(0,"b4", null, 0, null, null);
        commentB.add(fcgrB1);
        commentB.add(fcgrB2);
        commentB.add(fcgrB3);
        commentB.add(fcgrB4);

        given(mapper.selFeedCommentTopBy4ByFeedId(fgr1.getFeedId())).willReturn(commentA);
        given(mapper.selFeedCommentTopBy4ByFeedId(fgr2.getFeedId())).willReturn(commentB);

        List<FeedGetRes> res = service.getFeed(p);
        verify(mapper).selFeed(p);
        assertEquals(list.size(), res.size(), "1. 리턴값이 다름.");
        for (int i = 0; i < list.size(); i++) {
            FeedGetRes wantedRes = list.get(i);
            verify(mapper).selFeedPicsByFeedId(wantedRes.getFeedId());
            verify(mapper).selFeedCommentTopBy4ByFeedId(wantedRes.getFeedId());
        }

        FeedGetRes actualResA = list.get(0);
        assertEquals(picsA.size(), actualResA.getPics().size(), "fgr1의 이미지 다름");
        assertEquals(picsA, actualResA.getPics(), "fgr1의 이미지 다름");
        assertEquals(commentA, actualResA.getComments(), "fgr1의 댓글다름.");
        assertEquals(0, actualResA.getIsMoreComment());

        FeedGetRes actualResB = list.get(1);
        assertEquals(picsB.size(), actualResB.getPics().size(), "fgr1의 이미지 다름");
        assertEquals(picsB, actualResB.getPics(), "fgr1의 이미지 다름");
        assertEquals(commentB, actualResB.getComments(), "fgr1의 댓글다름.");
        assertEquals(3, commentB.size());
        assertEquals(1, actualResB.getIsMoreComment());
    }
}