package com.green.greengram.feedfavorite.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.beans.ConstructorProperties;

@Getter
@ToString
@EqualsAndHashCode
public class FeedFavoriteToggleReq {
    @Schema(name = "feed_id")
    private long feedId;
    @Schema(name = "user_id")
    private long userId;

    @ConstructorProperties({"feed_id", "user_id"})
    public FeedFavoriteToggleReq(long feedId,  long userId) {
        this.userId = userId;
        this.feedId = feedId;
    }
}
