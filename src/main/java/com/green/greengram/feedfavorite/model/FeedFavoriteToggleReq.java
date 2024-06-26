package com.green.greengram.feedfavorite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.beans.ConstructorProperties;

@Getter
@ToString
@EqualsAndHashCode
public class FeedFavoriteToggleReq {
    @Schema(name = "feed_id")
    private long feedId;

    //@Schema(name = "user_id")
    @JsonIgnore
    private long userId;

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @ConstructorProperties({"feed_id"})
    public FeedFavoriteToggleReq(long feedId) {
        this.feedId = feedId;
    }
}
