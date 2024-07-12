package com.green.greengram.feedcomment;

import com.green.greengram.BaseIntegrationTest;
import com.green.greengram.common.model.MyResponse;
import com.green.greengram.feedcomment.model.FeedCommentPostReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FeedCommentIntegrationTest extends BaseIntegrationTest {
    private final String BASE_URL = "/api/feed/comment";

    @Test
    @Rollback
    @DisplayName("feed-comment-post 댓글쓰기")
    public void testFeedCommentPost() throws Exception {
        FeedCommentPostReq p = new FeedCommentPostReq();
        p.setFeedId(11);
        p.setUserId(1);
        p.setComment("내용1");
        p.setFeedCommentId(1);
        String reqJson = om.writeValueAsString(p);

        MvcResult mr = mvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String resJson = mr.getResponse().getContentAsString();
        MyResponse<Integer> result = om.readValue(resJson, MyResponse.class);
        assertEquals(1, p.getFeedCommentId(), "먼가 이상");
    }
}
