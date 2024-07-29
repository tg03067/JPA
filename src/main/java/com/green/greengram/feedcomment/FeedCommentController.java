package com.green.greengram.feedcomment;

import com.green.greengram.common.model.MyResponse;
import com.green.greengram.entity.FeedComment;
import com.green.greengram.feedcomment.model.FeedCommentDeleteReq;
import com.green.greengram.feedcomment.model.FeedCommentGetRes;
import com.green.greengram.feedcomment.model.FeedCommentPostReq;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface FeedCommentController {
    MyResponse<Long> postFeedComment(FeedCommentPostReq p);
    MyResponse<List<FeedCommentGetRes>> getFeedCommentList(Long feedId);
    MyResponse<Integer> deleteFeedComment(FeedCommentDeleteReq p);
}
