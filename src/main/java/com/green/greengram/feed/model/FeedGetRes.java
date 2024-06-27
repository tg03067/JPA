package com.green.greengram.feed.model;

import com.green.greengram.feedcomment.model.FeedCommentGetRes;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FeedGetRes {
    private long feedId;
    private long writerId;
    private String writerNm;
    private String writerPic;
    private List<String> pics;
    private String createdAt;
    private String contents;
    private String location;

    private int isFav;
    private List<FeedCommentGetRes> comments;
    private int isMoreComment;
}
