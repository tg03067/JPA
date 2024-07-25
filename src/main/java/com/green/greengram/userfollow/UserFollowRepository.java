package com.green.greengram.userfollow;

import com.green.greengram.entity.User;
import com.green.greengram.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {
    void deleteUserFollowByFromUserAndToUser(User fromUser, User toUser) ;
    UserFollow findUserFollowByFromUserAndToUser(User fromUser, User toUser) ;
    // JPQL
    @Query("select uf from UserFollow uf where uf.fromUser.userId = :fromUserId and uf.toUser.userId = :toUserId")
    UserFollow findUserFollowByFromUserAndToUser(Long fromUserId, Long toUserId) ;
}
