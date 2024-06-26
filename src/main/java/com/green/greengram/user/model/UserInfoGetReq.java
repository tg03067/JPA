package com.green.greengram.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.BindParam;

import java.beans.ConstructorProperties;

@Getter
@Setter
@EqualsAndHashCode
public class UserInfoGetReq {
    @JsonIgnore
    //@Schema(name = "signed_user_id", defaultValue = "7", description = "로그인한 사용자 pk")
    private long signedUserId;
    @BindParam("profile_user_id")
    @Schema(name = "profile_user_id", defaultValue = "8", description = "프로필 사용자 pk")
    private long profileUserId;


    @ConstructorProperties({"profile_user_id"})
    public UserInfoGetReq( long profileUserId){
        this.profileUserId = profileUserId;
    }
}