package com.green.greengram.userfollow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengram.CharEncodingConfiguration;
import com.green.greengram.common.model.ResultDto;
import com.green.greengram.userfollow.model.UserFollowReq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(CharEncodingConfiguration.class)
@WebMvcTest(UserFollowControllerImpl.class)
class UserFollowControllerTest {
    @Autowired private ObjectMapper om;
    @Autowired private MockMvc mvc;
    @MockBean private UserFollowService service;
    private final String BASE_URL = "/api/user/follow";

    @Test
    void postUserFollow() throws Exception {
        UserFollowReq req = new UserFollowReq(1, 2);
        int result = 1;
        given(service.postUserFollow(req)).willReturn(result);
        String json = om.writeValueAsString(req);

        ResultDto<Integer> expectedResult = ResultDto.<Integer>builder().
                statusCode(HttpStatus.OK).
                resultMsg(HttpStatus.OK.toString()).
                resultData(result).
                build();

        Map expectedResultMap = new HashMap();
        expectedResultMap.put("statusCode", HttpStatus.OK);
        expectedResultMap.put("resultMsg", HttpStatus.OK.toString());
        expectedResultMap.put("resultData", result);

        String expectedJson = om.writeValueAsString(expectedResult);
        //String expectedJson = om.writeValueAsString(expectedResultMap);

        mvc.perform(
                post(BASE_URL).
                        contentType(MediaType.APPLICATION_JSON).
                        content(json)
        )
        .andExpect(status().isOk()) //200으로 넘어오는지 확인.
//        .andExpect(content().string(expectedJson)) //실제 넘어온 값, 99% Json형태.
        .andExpect(content().json(expectedJson))
        .andDo(print());

        verify(service).postUserFollow(req);
    }

    @Test
    void postUserFollow2() throws Exception {
        UserFollowReq req = new UserFollowReq(1, 2);
        int result = 10;
        given(service.postUserFollow(req)).willReturn(result);
        String json = om.writeValueAsString(req);

        ResultDto<Integer> expectedResult = ResultDto.<Integer>builder().
                statusCode(HttpStatus.OK).
                resultMsg(HttpStatus.OK.toString()).
                resultData(result).
                build();

        Map expectedResultMap = new HashMap();
        expectedResultMap.put("statusCode", HttpStatus.OK);
        expectedResultMap.put("resultMsg", HttpStatus.OK.toString());
        expectedResultMap.put("resultData", result);

        //String expectedJson = om.writeValueAsString(expectedResult);
        String expectedJson = om.writeValueAsString(expectedResultMap);

        mvc.perform(
                    post(BASE_URL).
                    contentType(MediaType.APPLICATION_JSON).
                    content(json)
                )
                .andExpect(status().isOk()) //200으로 넘어오는지 확인.
                .andExpect(content().json(expectedJson))
                .andDo(print());

        verify(service).postUserFollow(req);
    }

    @Test
    void deleteUserFollow() throws Exception {
        UserFollowReq req = new UserFollowReq(1, 2);
        int result = 1;
        given(service.deleteUserFollow(req)).willReturn(1);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("from_user_id", String.valueOf(req.getFromUserId()));
        params.add("to_user_id", String.valueOf(req.getToUserId()));

        ResultDto<Integer> expectedResult = ResultDto.<Integer>builder().
                statusCode(HttpStatus.OK).
                resultMsg(HttpStatus.OK.toString()).
                resultData(result).
                build();
        String expectedJson = om.writeValueAsString(expectedResult);
        mvc.perform(delete(BASE_URL).params(params))
        .andDo(print())
        .andExpect(content().json(expectedJson))
        .andExpect(status().isOk());

        verify(service).deleteUserFollow(req);
    }
    @Test
    void deleteUserFollow2() throws Exception {
        //바로날리기
        UserFollowReq req = new UserFollowReq(1, 2);
        int result = 1;
        given(service.deleteUserFollow(req)).willReturn(1);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("from_user_id", String.valueOf(req.getFromUserId()));
        params.add("to_user_id", String.valueOf(req.getToUserId()));

        ResultDto<Integer> expectedResult = ResultDto.<Integer>builder().
                statusCode(HttpStatus.OK).
                resultMsg(HttpStatus.OK.toString()).
                resultData(result).
                build();
        String expectedJson = om.writeValueAsString(expectedResult);
        mvc.perform(delete(BASE_URL + "?from_user_id=" + req.getFromUserId()
                        + "&to_user_id=" + req.getToUserId()))
                .andDo(print())
                .andExpect(content().json(expectedJson))
                .andExpect(status().isOk());

        verify(service).deleteUserFollow(req);
    }
}