package com.green.greengram.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRes {
    @Schema(example = "1", description = "유저PK")
    private long userId;
    @Schema(example = "홍길동", description = "유저 이름")
    private String nm;
    @Schema(example = "a5655288-e59f-4cd4-94fe-df793bed4a8a.png", description = "유저 프로필 이미지")
    private String pic;
}
