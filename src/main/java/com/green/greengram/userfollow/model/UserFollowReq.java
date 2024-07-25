package com.green.greengram.userfollow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.BindParam;

import java.beans.ConstructorProperties;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserFollowReq {
    @JsonIgnore
    private long fromUserId;

    @Schema(name = "to_user_id", example = "8", description = "팔로잉 아이디",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private long toUserId;

    @ConstructorProperties({"to_user_id"})
    public UserFollowReq(long toUserId) {
        this.toUserId = toUserId;
    }
}
