package com.green.greengram.feedcomment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class FeedCommentPostReq {
    @JsonIgnore
    private long feedCommentId;

    @Schema(defaultValue = "1")
    private long feedId;
    //@Schema(defaultValue = "8")
    @JsonIgnore
    private long userId;

    private String comment;

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
