package com.green.greengram.feedcomment;

import com.green.greengram.entity.Feed;
import com.green.greengram.entity.FeedComment;
import com.green.greengram.entity.User;
import com.green.greengram.exception.CustomException;
import com.green.greengram.exception.MemberErrorCode;
import com.green.greengram.feed.FeedRepository;
import com.green.greengram.feedcomment.model.FeedCommentDeleteReq;
import com.green.greengram.feedcomment.model.FeedCommentGetRes;
import com.green.greengram.feedcomment.model.FeedCommentPostReq;
import com.green.greengram.security.AuthenticationFacade;
import com.green.greengram.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FeedCommentServiceImpl implements FeedCommentService {
    private final FeedCommentMapper mapper ;
    private final AuthenticationFacade authenticationFacade ;
    private final FeedCommentRepository commentRepository ;
    private final UserRepository userRepository ;
    private final FeedRepository feedRepository ;

    @Override
    public long postFeedComment(FeedCommentPostReq p){
        // User Entity ( 영속성 )
        User user = userRepository.getReferenceById(authenticationFacade.getLoginUserId()) ;

        // Feed Entity ( 영속성 )
        Feed feed = feedRepository.getReferenceById(p.getFeedId()) ;

//        FeedComment fc = commentRepository.saveFeedComment(p.getFeedId(), user.getUserId(), p.getComment()) ;
//        log.info("p: {}", p.getFeedCommentId());
//        commentRepository.save(fc) ;

        FeedComment feedComment = new FeedComment() ;
        feedComment.setComment(p.getComment()) ;
        feedComment.setFeed(feed) ;
        feedComment.setUser(user) ;

        commentRepository.save(feedComment) ;

        return feedComment.getFeedCommentId() ;
    }
//    @Override
//    public int delFeedComment(FeedCommentDeleteReq p){
//        User user = userRepository.getReferenceById(authenticationFacade.getLoginUserId()) ;
//        FeedComment feedComment = commentRepository.findFeedCommentByUserIdAndFeedCommentId(user, p.getFeedCommentId()) ;
//        commentRepository.delete(feedComment) ;
//        return 1 ;
//    }
    @Override
    public int delFeedComment(FeedCommentDeleteReq p){
            FeedComment fc = commentRepository.getReferenceById(p.getFeedCommentId()) ;
            fc.getUser().getUserId() ; // 그래프 탐색이라 호칭
            if(fc.getUser().getUserId() != authenticationFacade.getLoginUserId()){
                throw new CustomException(MemberErrorCode.UNAUTHORIZED) ;
            }
            commentRepository.delete(fc) ;

            return 1 ;
        }
    @Override
    public List<FeedCommentGetRes> getFeedComment(Long feedId){
        Feed feed = new Feed() ;
        feed.setFeedId(feedId) ;
        List<FeedComment> list = commentRepository.findAllByFeedOrderByFeedCommentId(feed) ;

        List<FeedCommentGetRes> result = new ArrayList<>() ;
        for(FeedComment fc : list){
            FeedCommentGetRes item = new FeedCommentGetRes() ;
            result.add(item) ;

            item.setFeedCommentId(fc.getFeedCommentId()) ;
            item.setComment(fc.getComment()) ;
            item.setCreatedAt(fc.getCreatedAt().toString()) ;
            item.setWriterId(fc.getUser().getUserId()) ;
            item.setWriterNm(fc.getUser().getNm()) ;
            item.setWriterPic(fc.getUser().getPic()) ;
        }
        return result ;
    }
}
