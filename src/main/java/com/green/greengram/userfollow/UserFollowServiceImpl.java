package com.green.greengram.userfollow;

import com.green.greengram.entity.User;
import com.green.greengram.entity.UserFollow;
import com.green.greengram.security.AuthenticationFacade;
import com.green.greengram.userfollow.model.UserFollowReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserFollowServiceImpl implements UserFollowService{
    private final UserFollowMapper mapper;
    private final AuthenticationFacade authenticationFacade;
    private final UserFollowRepository userFollowRepository ;

    @Override
    public int postUserFollow(UserFollowReq p){
        long signedUserId = authenticationFacade.getLoginUserId() ;
        // p.setFromUserId(authenticationFacade.getLoginUserId()) ;
        User fromUser = new User() ;
        fromUser.setUserId(signedUserId) ;
        User toUser = new User() ;
        toUser.setUserId(p.getToUserId()) ;

        UserFollow userFollow = new UserFollow() ;
        userFollow.setFromUser(fromUser) ;
        userFollow.setToUser(toUser) ;

        userFollowRepository.save(userFollow) ;

        // return mapper.insUserFollow(p) ;
        return 1 ;
    }
    @Override
    public int deleteUserFollow(UserFollowReq p){
        long signedUserId = authenticationFacade.getLoginUserId() ;
        // p.setFromUserId(authenticationFacade.getLoginUserId()) ;
        User fromUser = new User() ;
        fromUser.setUserId(signedUserId) ;
        User toUser = new User() ;
        toUser.setUserId(p.getToUserId()) ;
        UserFollow userFollow = new UserFollow() ;
        userFollow.setFromUser(fromUser) ;
        userFollow.setToUser(toUser) ;
        userFollow = userFollowRepository.findUserFollowByFromUserAndToUser(fromUser, toUser) ;

        userFollowRepository.delete(userFollow) ;
        //userFollowRepository.deleteUserFollowByFromUserAndToUser(fromUser, toUser) ;
        // return mapper.delUserFollow(p) ;
        return 1 ;
    }
}
