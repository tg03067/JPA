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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
    @Autowired private ObjectMapper om; // json을 객체, object로 변환, 반대도 가능.
    @Autowired private MockMvc mvc; // postman 같은 역활
    private final String BASE_URL = "/api/feed/favorite";

    @Test
    void toggleFavorite() throws Exception {
        FeedFavoriteToggleReq req = new FeedFavoriteToggleReq(1, 2);
        int result = 1;
        given(service.toggleFavorite(req)).willReturn(result);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("feed_id", String.valueOf(req.getFeedId()));
        params.add("user_id", String.valueOf(req.getUserId()));

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
        params.add("feed_id", String.valueOf(req.getFeedId()));
        params.add("user_id", String.valueOf(req.getUserId()));

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
    void toggleFavorite3() throws Exception {
        FeedFavoriteToggleReq req = new FeedFavoriteToggleReq(1, 2);
        int resultData = 1;
        given(service.toggleFavorite(req)).willReturn(resultData);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("feed_id", String.valueOf(req.getFeedId()));
        params.add("user_id", String.valueOf(req.getUserId()));

        Map<String, Object> result1 = new HashMap<>();
        result1.put("statusCode", HttpStatus.OK);
        result1.put("resultMsg", "좋아요 처리");
        result1.put("resultData", resultData);

        String expectedResJson = om.writeValueAsString(result1);

        mvc.perform(get(BASE_URL).params(params)).
                andExpect(status().isOk()).
                andExpect(content().json(expectedResJson)).
                andDo(print());

        verify(service).toggleFavorite(req);
    }
}