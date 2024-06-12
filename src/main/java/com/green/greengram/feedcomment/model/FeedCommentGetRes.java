package com.green.greengram.feedcomment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class FeedCommentGetRes {
    private long feedCommentId;
    private String comment;
    private String createdAt;
    private long writerId;
    private String writerNm;
    private String writerPic;
}
