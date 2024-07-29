package com.green.greengram.feedcomment;

import com.green.greengram.entity.FeedComment;
import com.green.greengram.feedcomment.model.FeedCommentDeleteReq;
import com.green.greengram.feedcomment.model.FeedCommentGetRes;
import com.green.greengram.feedcomment.model.FeedCommentPostReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

public interface FeedCommentService {
    long postFeedComment(FeedCommentPostReq p);
    int delFeedComment(FeedCommentDeleteReq p);
    List<FeedCommentGetRes> getFeedComment(Long feedId);
}
