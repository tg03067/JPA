package com.green.greengram.feedfavorite;

import com.green.greengram.entity.FeedFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedFavoriteRepository extends JpaRepository<FeedFavorite, Long> {
    @Query(" select ff from FeedFavorite ff where ff.user.userId = :userId and ff.feed.feedId = :feedId")
    FeedFavorite findFeedFavoriteByUserIdAndFeedId(long userId, long feedId); ;
    @Query(value = " insert into feed_favorite (feed_id, user_id, creatad_at ) VALUES ( :feedId, :userId, now() ) ", nativeQuery = true)
    void saveFeedFavorite(long feedId, long userId) ;
}
