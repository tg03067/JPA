package com.green.greengram.userfollow;

import com.green.greengram.BaseIntegrationTest;
import com.green.greengram.common.model.MyResponse;
import com.green.greengram.userfollow.model.UserFollowReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserFollowIntegrationTest extends BaseIntegrationTest {
    private final String BASE_URL = "/api/user/follow";
    @Test
    @Rollback(value = false)
    @DisplayName("post - 유저팔로우")
    public void postUserFollow() throws Exception {
        UserFollowReq p = new UserFollowReq(4);
        String reqJson = om.writeValueAsString(p);

        MvcResult mr = mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(reqJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String resContent = mr.getResponse().getContentAsString();
        MyResponse<Integer> result = om.readValue(resContent, MyResponse.class); // 객체를 string으로 바꿈.
        assertEquals(1, result.getResultData());
    }

    @Test
    @Rollback(value = false)
    @DisplayName("delete - 유저팔로우")
    public void deleteUserFollow() throws Exception {
        UserFollowReq p = new UserFollowReq(1);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("from_user_id", String.valueOf(p.getFromUserId()));
        params.add("to_user_id", String.valueOf(p.getToUserId()));

        MvcResult mr = mvc.perform(delete(BASE_URL).params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String resContent = mr.getResponse().getContentAsString();
        MyResponse<Integer> result = om.readValue(resContent, MyResponse.class);
        assertEquals(1, result.getResultData(), "먼가 이상");
    }
}
