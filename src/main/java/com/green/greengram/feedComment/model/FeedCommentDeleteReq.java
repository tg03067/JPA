package com.green.greengram.feedComment.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

import java.beans.ConstructorProperties;

@Getter
public class FeedCommentDeleteReq {
    @Schema(name = "feed_comment_id")
    private long feedCommentId;
    @Schema(name = "signed_user_id")
    private long signedUserId;

    @ConstructorProperties({"feed_comment_id", "signed_user_id"})
    public FeedCommentDeleteReq(long feedCommentId, long signedUserId) {
        this.feedCommentId = feedCommentId;
        this.signedUserId = signedUserId;
    }
}
