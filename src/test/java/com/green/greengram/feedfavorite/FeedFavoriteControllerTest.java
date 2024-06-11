package com.green.greengram.feedfavorite;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengram.CharEncodingConfiguration;
import com.green.greengram.common.model.ResultDto;
import com.green.greengram.feedfavorite.model.FeedFavoriteToggleReq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(CharEncodingConfiguration.class)
@WebMvcTest(FeedFavoriteControllerImpl.class)
class FeedFavoriteControllerTest {
    @MockBean private FeedFavoriteService service;
    @Autowired private ObjectMapper om;
    @Autowired private MockMvc mvc;
    private final String BASE_URL = "/api/feed/favorite";

    @Test
    void toggleFavorite() throws Exception {
        FeedFavoriteToggleReq req = new FeedFavoriteToggleReq(1, 2);
        int result = 1;
        given(service.toggleFavorite(req)).willReturn(result);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("feedId", String.valueOf(req.getFeedId()));
        params.add("userId", String.valueOf(req.getUserId()));

        ResultDto<Integer> expectedResult = ResultDto.<Integer>builder().
                statusCode(HttpStatus.OK).
                resultMsg(result == 1 ? "좋아요 처리" : "좋아요 취소").
                resultData(result).
                build();
        String expectedJson = om.writeValueAsString(expectedResult);
        mvc.perform(get(BASE_URL).params(params)).
                andExpect(content().json(expectedJson)).
                andExpect(status().isOk()).
                andDo(print());

        verify(service).toggleFavorite(req);
    }

    @Test
    void toggleFavorite2() throws Exception {
        FeedFavoriteToggleReq req = new FeedFavoriteToggleReq(1, 2);
        int result = 0;
        given(service.toggleFavorite(req)).willReturn(result);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("feedId", String.valueOf(req.getFeedId()));
        params.add("userId", String.valueOf(req.getUserId()));

        ResultDto<Integer> expectedResult = ResultDto.<Integer>builder().
                statusCode(HttpStatus.OK).
                resultMsg(result == 1 ? "좋아요 처리" : "좋아요 취소").
                resultData(result).
                build();
        String expectedJson = om.writeValueAsString(expectedResult);
        mvc.perform(get(BASE_URL).params(params)).
                andExpect(content().json(expectedJson)).
                andExpect(status().isOk()).
                andDo(print());

        verify(service).toggleFavorite(req);
    }
}