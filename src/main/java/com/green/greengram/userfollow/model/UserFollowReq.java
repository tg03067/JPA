package com.green.greengram.userfollow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.BindParam;

@Getter
@Setter
public class UserFollowReq {
    @Schema(example = "7", description = "팔로워 아이디", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private long fromUserId;
    @Schema(example = "8", description = "팔로잉 아이디", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private long toUserId;
}
