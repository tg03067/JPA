package com.green.greengram.feedFavorite;

import com.green.greengram.feedFavorite.model.FeedFavoriteToggleReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FeedFavoriteMapper {
    int insFeedFavorite(FeedFavoriteToggleReq p);
    int delFeedFavorite(FeedFavoriteToggleReq p);
}
