package com.green.greengram.feedComment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FeedCommentPostReq {
    @JsonIgnore
    private long feedCommentId;
    @Schema(defaultValue = "1")
    private long feedId;
    @Schema(defaultValue = "8")
    private long userId;
    private String comment;
}
