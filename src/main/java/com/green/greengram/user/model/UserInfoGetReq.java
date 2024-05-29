package com.green.greengram.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Getter
@EqualsAndHashCode
public class UserInfoGetReq {
    @Schema(name = "signed_user_id", defaultValue = "7", description = "로그인한 사용자 pk")
    private long signedUserId;
    @Schema(name = "profile_user_id", defaultValue = "8", description = "프로필 사용자 pk")
    private long profileUserId;

    @ConstructorProperties({"signed_user_id", "profile_user_id"})
    public UserInfoGetReq(long signedUserId, long profileUserId){
        this.profileUserId = profileUserId;
        this.signedUserId = signedUserId;
    }
}