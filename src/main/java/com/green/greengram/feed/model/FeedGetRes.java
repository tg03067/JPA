package com.green.greengram.feed.model;

import com.green.greengram.feedComment.model.FeedCommentGetRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class FeedGetRes {
    private long feedId;
    private long writerId;
    private String writerNm;
    private String writerPic;
    private String contents;
    private String location;
    private String createdAt;
    private List<String> pics;

    private int isFav;
    private List<FeedCommentGetRes> comments;
    private int isMoreComment;
}
