package com.green.greengram.feed;

import com.green.greengram.feed.model.*;
import com.green.greengram.feed.model.FeedPostReq;
import com.green.greengram.feedcomment.model.FeedCommentGetRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedMapper {
    int postFeed(FeedPostReq p);
    int postFeedPics(FeedPicPostDto p);

    List<FeedGetRes> selFeed(FeedGetReq p);
    List<String> selFeedPicsByFeedId(long feedId);

    List<FeedCommentGetRes> selFeedCommentTopBy4ByFeedId(long feedId);
}
