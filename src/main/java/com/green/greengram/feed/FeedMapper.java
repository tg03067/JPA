package com.green.greengram.feed;

import com.green.greengram.feed.model.*;
import com.green.greengram.feed.model.FeedPostReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedMapper {
    int postFeed(FeedPostReq p);
    int postFeedPics(FeedPicPostDto p);
    List<FeedGetRes> getFeed(FeedGetReq p);
    List<String> getFeedPicsByFeedId(long feedId);
}
