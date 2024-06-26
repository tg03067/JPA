package com.green.greengram.feedcomment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.beans.ConstructorProperties;

@Getter
@EqualsAndHashCode
public class FeedCommentDeleteReq {
    @Schema(name = "feed_comment_id")
    private long feedCommentId;
    //@Schema(name = "signed_user_id")
    @JsonIgnore
    private long signedUserId;

    public void setSignedUserId(long signedUserId) {
        this.signedUserId = signedUserId;
    }

    @ConstructorProperties({"feed_comment_id"})
    public FeedCommentDeleteReq(long feedCommentId) {
        this.feedCommentId = feedCommentId;
    }
}
