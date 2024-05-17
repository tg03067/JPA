package com.green.greengram.feed;

import com.green.greengram.common.CustomFileUtils;
import com.green.greengram.common.GlobalConst;
import com.green.greengram.feed.model.*;
import com.green.greengram.feedComment.model.FeedCommentGetRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedService {
    private final FeedMapper mapper;
    private final CustomFileUtils customFileUtils ;

    @Transactional
    public FeedPostRes postFeed(List<MultipartFile> pics, FeedPostReq p){ //feed pk 값 리턴.
        int result = mapper.postFeed(p);
        log.info(String.format("%d",result));
        FeedPicPostDto picDto = FeedPicPostDto.builder().feedId(p.getFeedId()).build();
        try {
            String path = String.format("feed/%d", p.getFeedId());
            customFileUtils.makeFolders(path);
            for(MultipartFile pic : pics) {
                String saveFileName = customFileUtils.makeRandomFileName(pic);
                picDto.getFileNames().add(saveFileName);
                String target = String.format("%s/%s", path, saveFileName);
                customFileUtils.transferTo(pic, target);
            }
            int affectedRowsPics = mapper.postFeedPics(picDto);
            log.info(String.format("%s",affectedRowsPics));
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Feed 등록 오류");
        }
        return FeedPostRes.builder()
                .feedId(p.getFeedId())
                .pics(picDto.getFileNames())
                .build();
    }

    public List<FeedGetRes> getFeed(FeedGetReq p){
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