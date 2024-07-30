package com.green.greengram.feedcomment;

import com.green.greengram.entity.Feed;
import com.green.greengram.entity.FeedComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {
    // FeedComment findFeedCommentByUserIdAndFeedCommentId(User userId, Long commentId) ;

    Page<FeedComment> findAllByFeedOrderByFeedCommentId(Feed feedId, Pageable pageable) ;

    @Query(value = " select fc.feed_comment_id as feedCommentId " +
                            ", fc.comment as comment " +
                            ", u.user_id as writerId " +
                            ", u.nm as writerNm " +
                            ", u.pic as writerPic " +
                            ", fc.created_at as createdAt " +
                            ", fc.updated_at as updatedAt " +
            "from feed_comment AS fc " +
            "INNER JOIN feed AS f " +
            "on fc.feed_id = f.feed_id " +
            "INNER JOIN user AS u " +
            "on fc.user_id = u.user_id " +
            "where fc.feed_id = :feedId " +
            "order by fc.feed_comment_id ASC " +
            "limit 3, 9999", nativeQuery = true)
    List<FeedCommentGetResInterpace> findAllByFeedCommentLimit3AndInfinity(long feedId) ;

    List<FeedComment> findAllByFeedOrderByFeedCommentId(Feed feedId) ;
}
