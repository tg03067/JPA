package com.green.greengram.feedfavorite;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengram.CharEncodingConfiguration;
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
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(CharEncodingConfiguration.class)
@WebMvcTest(FeedFavoriteControllerImpl.class)
class FeedFavoriteControllerTest2 {
    @MockBean private FeedFavoriteService service;
    @Autowired private ObjectMapper om; // json을 객체, object로 변환, 반대도 가능.
    @Autowired private MockMvc mvc; // postman 같은 역활
    private final String BASE_URL = "/api/feed/favorite";

    void proc ( FeedFavoriteToggleReq req, Map<String, Object> result ) throws Exception {
        int resultData = (int)result.get("resultData");
        given(service.toggleFavorite(req)).willReturn(resultData);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("feed_id", String.valueOf(req.getFeedId()));
        params.add("user_id", String.valueOf(req.getUserId()));
        String expectedResJson = om.writeValueAsString(result);
        mvc.perform(get(BASE_URL).params(params)).
                andExpect(status().isOk()).
                andExpect(content().json(expectedResJson)).
                andDo(print());

        verify(service).toggleFavorite(req);
    }

    @Test
    void toggleFavorite3() throws Exception {
        FeedFavoriteToggleReq req = new FeedFavoriteToggleReq(1);
        int resultData = 1;
        Map<String, Object> result1 = new HashMap<>();
        result1.put("statusCode", HttpStatus.OK);
        result1.put("resultMsg", "좋아요 처리");
        result1.put("resultData", resultData);
        proc(req, result1);
    }
    @Test
    void toggleFavorite2() throws Exception {
        FeedFavoriteToggleReq req = new FeedFavoriteToggleReq(2);
        int resultData = 2;
        Map<String, Object> result1 = new HashMap<>();
        result1.put("statusCode", HttpStatus.OK);
        result1.put("resultMsg", "좋아요 처리");
        result1.put("resultData", resultData);
        proc(req, result1);
    }

    @Test
    void toggleFavorite4() throws Exception {
        FeedFavoriteToggleReq req = new FeedFavoriteToggleReq(5);
        //resultMsg값이 "좋아요 취소"가 리턴이 되는지 확인
        int resultData = 0;
        Map<String, Object> result1 = new HashMap<>();
        result1.put("statusCode", HttpStatus.OK);
        result1.put("resultMsg", "좋아요 취소");
        result1.put("resultData", resultData);

        proc(req, result1);
    }
}