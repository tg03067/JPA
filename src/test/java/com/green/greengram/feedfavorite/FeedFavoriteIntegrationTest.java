package com.green.greengram.feedfavorite;

import com.green.greengram.BaseIntegrationTest;
import com.green.greengram.common.model.ResultDto;
import com.green.greengram.feedfavorite.model.FeedFavoriteToggleReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FeedFavoriteIntegrationTest extends BaseIntegrationTest {
    private final String BASE_URL = "/api/feed/favorite";
    @Test
    @Rollback(value = false)
    @DisplayName("favorite-toggle-delete 좋아요 취소 토글")
    public void favoriteToggle() throws Exception {
        FeedFavoriteToggleReq p = new FeedFavoriteToggleReq(1);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("feed_id", String.valueOf(p.getFeedId()));
        params.add("user_id", String.valueOf(p.getUserId()));

        MvcResult mr = mvc.perform(get(BASE_URL).params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String resJson = mr.getResponse().getContentAsString();
        ResultDto<Integer> result = om.readValue(resJson, ResultDto.class);
        assertEquals(0, result.getResultData());
        assertEquals("좋아요 취소", result.getResultMsg());
    }

    @Test
    @Rollback(value = false)
    @DisplayName("favorite-toggle-post 좋아요 처리 토글")
    public void favoriteToggle2() throws Exception {
        FeedFavoriteToggleReq p = new FeedFavoriteToggleReq(6);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("feed_id", String.valueOf(p.getFeedId()));
        params.add("user_id", String.valueOf(p.getUserId()));

        MvcResult mr = mvc.perform(get(BASE_URL).params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String resJson = mr.getResponse().getContentAsString();
        ResultDto<Integer> result = om.readValue(resJson, ResultDto.class);
        assertEquals(1, result.getResultData());
        assertEquals("좋아요 처리", result.getResultMsg());
    }
}
