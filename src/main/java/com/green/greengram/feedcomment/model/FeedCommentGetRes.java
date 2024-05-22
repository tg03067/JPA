package com.green.greengram.feedcomment.model;

import lombok.Data;

@Data
public class FeedCommentGetRes {
    private long feedCommentId;
    private String comment;
    private String createdAt;
    private long writerId;
    private String writerNm;
    private String writerPic;
}
