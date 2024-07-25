package com.green.greengram.userfollow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class UserFollowPostReq {
    // 필드 주입 ( setter, 생성자가 아닌 reflection API 를 사용하여 접근제어자 ( private ) 을 무력화 시켜서 주소값을 주입 )
    @Schema(example = "8", description = "팔로잉 아이디", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private long toUserId ;
}
