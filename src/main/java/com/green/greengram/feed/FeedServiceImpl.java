package com.green.greengram.feed;

import com.green.greengram.common.CustomFileUtils;
import com.green.greengram.common.GlobalConst;
import com.green.greengram.entity.Feed;
import com.green.greengram.entity.FeedPics;
import com.green.greengram.entity.User;
import com.green.greengram.feed.model.*;
import com.green.greengram.feedcomment.model.FeedCommentGetRes;
import com.green.greengram.security.AuthenticationFacade;
import com.green.greengram.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {
    private final FeedMapper mapper ;
    private final CustomFileUtils customFileUtils ;
    private final AuthenticationFacade authenticationFacade ;
    private final FeedRepository repository ;
    private final FeedPicsRepository feedPicsRepository ;
    private final UserRepository userRepository ;

    @Override @Transactional
    public FeedPostRes postFeed(List<MultipartFile> pics, FeedPostReq p){ //feed pk 값 리턴.
        User user = userRepository.getReferenceById(authenticationFacade.getLoginUserId()) ;
        log.info("user: {}", user);

        Feed feed = new Feed() ;
        feed.setUser(user) ;
        feed.setContents(p.getContents()) ;
        feed.setLocation(p.getLocation()) ;
        repository.save(feed) ;

        log.info("feed: {}", feed);

        if(pics == null){
            return FeedPostRes.builder()
                    .feedId(feed.getFeedId())
                    .build() ;
        }

        List<String> picListName = new ArrayList<>() ;

        try {
            List<FeedPics> picsList = new ArrayList<>() ;
            String path = String.format("feed/%d", feed.getFeedId());
            customFileUtils.makeFolders(path);
            for(MultipartFile pic : pics) {
                String saveFileName = customFileUtils.makeRandomFileName(pic);
                String target = String.format("%s/%s", path, saveFileName);
                customFileUtils.transferTo(pic, target);

                log.info("Saved file to: {}", target);

                picListName.add(saveFileName);

                FeedPics feedPics = new FeedPics() ;
                feedPics.setPic(saveFileName) ;
                feedPics.setFeed(feed) ;

                picsList.add(feedPics) ;
                feedPicsRepository.save(feedPics) ;
            }
            /*
                JPA Batch Insert(대량 insert) 는 쓰기 지연을 사용해 동작.
                이는 DB에 insert 후 id가 할당되는 identity 전략을 사용하면 사용할 수 없다는 의미
                yaml 파일에 " # JPA Batch 작업 (Insert, Update) "  주석인 부분과
                DB URL 에 파라미터 값 추가 " rewriteBatchedStatements = true "
             */
            log.info("feedPics: {}", picsList) ;
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Feed 등록 오류");
        }

        return FeedPostRes.builder()
                .feedId(feed.getFeedId())
                .pics(picListName)
                .build();
    }
    @Override
    public List<FeedGetRes> getFeed(FeedGetReq p){
        p.setSignedUserId(authenticationFacade.getLoginUserId());
        List<FeedGetRes> list = mapper.selFeed(p);

        if(list == null){
            return Collections.emptyList();
        }
        for(FeedGetRes res : list){
            //피드 하나당 포함된
            //사진 리스트
            List<String> pics = mapper.selFeedPicsByFeedId(res.getFeedId());
            res.setPics(pics);

            //댓글 리스트
            List<FeedCommentGetRes> comment = mapper.selFeedCommentTopBy4ByFeedId(res.getFeedId());
            boolean hasMoreComment = comment.size() == GlobalConst.COMMENT_SIZE_PER_FEED ;
            res.setIsMoreComment( hasMoreComment ? 1 : 0 );
            if(hasMoreComment){
                comment.remove(comment.size() - 1);
            }
            res.setComments(comment);
        }
        return list;
    }
}
//    @Override
//    public List<FeedGetRes> getFeed(FeedGetReq p){
//        List<FeedGetRes> list = mapper.selFeed(p);
//        if(list == null){
//            return Collections.emptyList();
//        }
//        for (FeedGetRes res : list){
//            List<String> pics = mapper.selFeedPicsByFeedId(res.getFeedId());
//            res.setPics(pics);
//            List<FeedCommentGetRes> com = mapper.selFeedCommentTopBy4ByFeedId(res.getFeedId());
//            if(com.size() == GlobalConst.COMMENT_SIZE_PER_FEED){
//                res.setIsMoreComment(1);
//                com.remove(com.size()-1);
//            }
//            res.setComments(com);
//        }
//        return list;
//    }
/*
    @Transactional
    public FeedPostRes postFeed(List<MultipartFile> pics, FeedPostReq p){
        int result = mapper.postFeed(p);
        FeedPicPostDto picDto = FeedPicPostDto.builder().feedId(p.getFeedId()).build();
        try {
            String path = String.format("feed/%d", p.getUserId());
            customFileUtils.makeFolders(path);
            for(MultipartFile pic : pics){
                String saveFileName = customFileUtils.makeRandomFileName(pic);
                picDto.getFileNames().add(saveFileName);
                String target = String.format("%s/%s",path, saveFileName);
                customFileUtils.transferTo(pic, target);
            }
            int affectedRowsPics = mapper.postFeedPics(picDto);

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Feed 등록 오류");
        }
        return FeedPostRes.builder().
                feedId(p.getFeedId()).
                pics(picDto.getFileNames()).
                build();
    }
 */