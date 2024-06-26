package com.green.greengram.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserProfilePatchReq {
    @JsonIgnore
    private long signedUserId;
    private MultipartFile pic;

    @JsonIgnore
    private String picName;
}
