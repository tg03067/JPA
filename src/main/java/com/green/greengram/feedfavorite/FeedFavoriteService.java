package com.green.greengram.feedfavorite;

import com.green.greengram.feedfavorite.model.FeedFavoriteToggleReq;

public interface FeedFavoriteService {
    int insFeedFavorite(FeedFavoriteToggleReq p);
    int toggleFavorite(FeedFavoriteToggleReq p);
}
