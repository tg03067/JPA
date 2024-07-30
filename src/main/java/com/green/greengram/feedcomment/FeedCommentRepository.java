package com.green.greengram.feedcomment;

import com.green.greengram.entity.Feed;
import com.green.greengram.entity.FeedComment;
import com.green.greengram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {
    // FeedComment findFeedCommentByUserIdAndFeedCommentId(User userId, Long commentId) ;

    List<FeedComment> findAllByFeedOrderByFeedCommentId(Feed feedId) ;

    /*
    INSERT INTO feed_comment
        SET feed_id = #{feedId}
        , user_id = #{userId}
        , comment = #{comment}
     */
}
