package com.green.greengram.feedComment;

import com.green.greengram.feedComment.model.FeedCommentDeleteReq;
import com.green.greengram.feedComment.model.FeedCommentGetRes;
import com.green.greengram.feedComment.model.FeedCommentPostReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedCommentMapper {
    int postFeedComment(FeedCommentPostReq p);
    int delFeedComment(FeedCommentDeleteReq p);
    List<FeedCommentGetRes> selFeedComment(Long feedId);
}
