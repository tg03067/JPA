package com.green.greengram.feedfavorite;

import com.green.greengram.entity.Feed;
import com.green.greengram.entity.FeedFavorite;
import com.green.greengram.entity.User;
import com.green.greengram.feed.FeedRepository;
import com.green.greengram.feedfavorite.model.FeedFavoriteToggleReq;
import com.green.greengram.security.AuthenticationFacade;
import com.green.greengram.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedFavoriteServiceImpl implements FeedFavoriteService{
    private final FeedFavoriteMapper mapper ;
    private final AuthenticationFacade authentication ;
    private final UserRepository userRepository ;
    private final FeedFavoriteRepository favoriteRepository ;
    private final FeedRepository feedRepository ;

    @Override
    public int insFeedFavorite(FeedFavoriteToggleReq p){
        return mapper.insFeedFavorite(p);
    }
//
//    @Override
//    public int toggleFavorite(FeedFavoriteToggleReq p){
//        long signedUserId = authentication.getLoginUserId() ;
//        FeedFavorite feedFavoriteRepository = favoriteRepository.findFeedFavoriteByUserIdAndFeedId(signedUserId, p.getFeedId()) ;
//        if(feedFavoriteRepository == null) {
//            User user = userRepository.getReferenceById(signedUserId) ;
//            Feed feed = feedRepository.getReferenceById(p.getFeedId()) ;
//            FeedFavorite feedFavorite = new FeedFavorite() ;
//            feedFavorite.setUser(user) ;
//            feedFavorite.setFeed(feed) ;
//            favoriteRepository.save(feedFavorite);
//            return 1;
//        }
//        favoriteRepository.delete(feedFavoriteRepository) ;
//        return 0 ;
//    }

    @Override
    public int toggleFavorite(FeedFavoriteToggleReq p){
        FeedFavorite feedFavoriteRepository = favoriteRepository.findFeedFavoriteByUserIdAndFeedId(authentication.getLoginUserId(), p.getFeedId()) ;
        if(feedFavoriteRepository == null){
            favoriteRepository.saveFeedFavorite(authentication.getLoginUserId(), p.getFeedId()) ;
            return 1 ;
        }
        favoriteRepository.delete(feedFavoriteRepository) ;
        return 0 ;
    }
}
