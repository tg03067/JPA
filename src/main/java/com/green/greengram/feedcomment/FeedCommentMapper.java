package com.green.greengram.feedcomment;

import com.green.greengram.feedcomment.model.FeedCommentDeleteReq;
import com.green.greengram.feedcomment.model.FeedCommentGetRes;
import com.green.greengram.feedcomment.model.FeedCommentPostReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedCommentMapper {
    int postFeedComment(FeedCommentPostReq p);
    int delFeedComment(FeedCommentDeleteReq p);
    List<FeedCommentGetRes> selFeedComment(Long feedId);
}
