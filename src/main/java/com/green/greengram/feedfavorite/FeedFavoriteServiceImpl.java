package com.green.greengram.feedfavorite;

import com.green.greengram.feedfavorite.model.FeedFavoriteToggleReq;
import com.green.greengram.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedFavoriteServiceImpl implements FeedFavoriteService{
    private final FeedFavoriteMapper mapper ;
    private final AuthenticationFacade authentication;
    @Override
    public int insFeedFavorite(FeedFavoriteToggleReq p){
        return mapper.insFeedFavorite(p);
    }

    //방법론 (1)
    //select로 레코드 확인
    //레코드가 있으면 delete, return 0
    //레코드가 있으면 insert, return 1

    //방법론 (2)
    //insert 에러가 터짐 > delete

    //방법론 (3)
    //delete > 1 > return 0;
    //delete > 0 > insert > return 1;
    @Override
    public int toggleFavorite(FeedFavoriteToggleReq p){
        p.setUserId(authentication.getLoginUserId());
        int delAffectedRows = mapper.delFeedFavorite(p);
        if(delAffectedRows == 1){
            return 0;
        }
        return mapper.insFeedFavorite(p);
    }
}
