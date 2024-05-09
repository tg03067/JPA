package com.green.greengram.feedFavorite.model;

import lombok.Data;

@Data
public class FeedFavoriteToggleReq {
    private long feedId;
    private long userId;
}
