package com.green.greengram.feedcomment;

import com.green.greengram.feedcomment.model.FeedCommentDeleteReq;
import com.green.greengram.feedcomment.model.FeedCommentGetRes;
import com.green.greengram.feedcomment.model.FeedCommentPostReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FeedCommentServiceImpl {
    private final FeedCommentMapper mapper ;

    public long postFeedComment(FeedCommentPostReq p){
        int affectedRows = mapper.postFeedComment(p);
        return p.getFeedCommentId();
    }

    public int delFeedComment(FeedCommentDeleteReq p){
        return mapper.delFeedComment(p);
    }

    public List<FeedCommentGetRes> getFeedComment(Long feedId){
        return mapper.selFeedComment(feedId);
    }
}
