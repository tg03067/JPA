package com.green.greengram.feedComment;

import com.green.greengram.feedComment.model.FeedCommentDeleteReq;
import com.green.greengram.feedComment.model.FeedCommentPostReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FeedCommentMapper {
    int postFeedComment(FeedCommentPostReq p);
    int delFeedComment(FeedCommentDeleteReq p);
}
