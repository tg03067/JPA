package com.green.greengram.userfollow;

import com.green.greengram.entity.User;
import com.green.greengram.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {
    void deleteUserFollowByFromUserAndToUser(User fromUser, User toUser) ;
    UserFollow findUserFollowByFromUserAndToUser(User fromUser, User toUser);
}
