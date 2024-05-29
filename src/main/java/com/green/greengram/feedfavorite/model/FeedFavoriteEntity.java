package com.green.greengram.feedfavorite.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class FeedFavoriteEntity {
    private long feedId;
    private long userId;
    private String createdAt;
}
