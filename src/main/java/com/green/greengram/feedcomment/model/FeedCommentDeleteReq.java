package com.green.greengram.feedcomment.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.beans.ConstructorProperties;

@Getter
public class FeedCommentDeleteReq {
    @Schema(name = "feed_comment_id")
    private Long feedCommentId;
    @Schema(name = "signed_user_id")
    private Long signedUserId;

    @ConstructorProperties({"feed_comment_id", "signed_user_id"})
    public FeedCommentDeleteReq(Long feedCommentId, Long signedUserId) {
        this.feedCommentId = feedCommentId;
        this.signedUserId = signedUserId;
    }
}
