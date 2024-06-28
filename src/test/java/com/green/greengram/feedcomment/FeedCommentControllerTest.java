package com.green.greengram.feedcomment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengram.CharEncodingConfiguration;
import com.green.greengram.feedcomment.model.FeedCommentDeleteReq;
import com.green.greengram.feedcomment.model.FeedCommentGetRes;
import com.green.greengram.feedcomment.model.FeedCommentPostReq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(CharEncodingConfiguration.class)
@WebMvcTest(FeedCommentControllerImpl.class)
class FeedCommentControllerTest {
    @Autowired private ObjectMapper om;
    @Autowired private MockMvc mvc;
    @MockBean private FeedCommentService service;
    private final String BASE_URL = "/api/feed/comment";



    @Test
    void postFeedComment() throws Exception {
        FeedCommentPostReq p = new FeedCommentPostReq();
        p.setUserId(1);
        p.setFeedId(2);
        p.setComment("안녕");
        long affectedRows = 1;

        given(service.postFeedComment(p)).willReturn(affectedRows);
        String json = om.writeValueAsString(p);

        Map<String, Object> expectedMap = new HashMap();
        expectedMap.put("statusCode", HttpStatus.OK);
        expectedMap.put("resultMsg", HttpStatus.OK.toString());
        expectedMap.put("resultData", affectedRows);
        String expectedJson = om.writeValueAsString(expectedMap);

        mvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andDo(print());

        verify(service).postFeedComment(p);
    }

    @Test
    void postFeedComment2() throws Exception {
        FeedCommentPostReq p = new FeedCommentPostReq();
        p.setUserId(2);
        p.setFeedId(3);
        p.setComment("반가워");

        long affectedRows = 5;

        given(service.postFeedComment(p)).willReturn(affectedRows);
        String json = om.writeValueAsString(p);

        Map<String, Object> expectedMap = new HashMap();
        expectedMap.put("statusCode", HttpStatus.OK);
        expectedMap.put("resultMsg", HttpStatus.OK.toString());
        expectedMap.put("resultData", affectedRows);
        String expectedJson = om.writeValueAsString(expectedMap);

        mvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andDo(print());

        verify(service).postFeedComment(p);
    }

    @Test
    void getFeedCommentList() throws Exception {
        long feedId = 1;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("feed_id", String.valueOf(feedId));

        List<FeedCommentGetRes> list = new ArrayList<>();

        FeedCommentGetRes res1 = new FeedCommentGetRes();
        FeedCommentGetRes res2 = new FeedCommentGetRes();
        FeedCommentGetRes res3 = new FeedCommentGetRes();
        list.add(res1);
        list.add(res2);
        list.add(res3);

        given(service.getFeedComment(feedId)).willReturn(list);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("statusCode", HttpStatus.OK);
        result.put("resultMsg", String.format("rows: %,d", list.size()));
        result.put("resultData", list);

        String expectedResJson = om.writeValueAsString(result);

        mvc.perform(get(BASE_URL).params(params))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson))
                .andDo(print());

        verify(service).getFeedComment(feedId);
    }

    @Test
    void getFeedCommentList2() throws Exception {
        long feedId = 5;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("feed_id", String.valueOf(feedId));

        List<FeedCommentGetRes> list = new ArrayList<>();

        FeedCommentGetRes res1 = new FeedCommentGetRes();
        FeedCommentGetRes res2 = new FeedCommentGetRes();
        FeedCommentGetRes res3 = new FeedCommentGetRes();
        FeedCommentGetRes res4 = new FeedCommentGetRes();
        list.add(res1);
        list.add(res2);
        list.add(res3);
        list.add(res4);

        given(service.getFeedComment(feedId)).willReturn(list);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("statusCode", HttpStatus.OK);
        result.put("resultMsg", String.format("rows: %,d", list.size()));
        result.put("resultData", list);

        String expectedResJson = om.writeValueAsString(result);

        mvc.perform(get(BASE_URL).params(params))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson))
                .andDo(print());

        verify(service).getFeedComment(feedId);
    }

    @Test
    void deleteFeedComment() throws Exception {
        FeedCommentDeleteReq req = new FeedCommentDeleteReq(1, 2);
        int result = 1;
        given(service.delFeedComment(req)).willReturn(result);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("feed_comment_id", String.valueOf(req.getFeedCommentId()));
        params.add("signed_user_id", String.valueOf(req.getSignedUserId()));

        Map<String, Object> resultRes = new HashMap<>();
        resultRes.put("statusCode", HttpStatus.OK);
        resultRes.put("resultMsg", result == 0 ? "댓글 삭제를 할 수 없습니다." : "댓글을 삭제하였습니다.");
        resultRes.put("resultData", result);

        String expectedResJson = om.writeValueAsString(resultRes);

        mvc.perform(delete(BASE_URL).params(params))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson))
                .andDo(print());

        verify(service).delFeedComment(req);
    }

    @Test
    void deleteFeedComment2() throws Exception {
        FeedCommentDeleteReq req = new FeedCommentDeleteReq(2, 3);
        int result = 0;
        given(service.delFeedComment(req)).willReturn(result);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("feed_comment_id", String.valueOf(req.getFeedCommentId()));
        params.add("signed_user_id", String.valueOf(req.getSignedUserId()));

        Map<String, Object> resultRes = new HashMap<>();
        resultRes.put("statusCode", HttpStatus.OK);
        resultRes.put("resultMsg", result == 0 ? "댓글 삭제를 할 수 없습니다." : "댓글을 삭제하였습니다.");
        resultRes.put("resultData", result);

        String expectedResJson = om.writeValueAsString(resultRes);

        mvc.perform(delete(BASE_URL).params(params))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson))
                .andDo(print());

        verify(service).delFeedComment(req);
    }
}