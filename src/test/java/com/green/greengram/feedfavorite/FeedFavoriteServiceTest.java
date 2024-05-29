package com.green.greengram.feedfavorite;

import com.green.greengram.feedfavorite.model.FeedFavoriteToggleReq;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@Import({FeedFavoriteServiceImpl.class})
class FeedFavoriteServiceTest {
    @MockBean
    private FeedFavoriteMapper mapper;
    @Autowired
    private FeedFavoriteServiceImpl service;

    @Test
    void toggleFavoriteIns() {
        FeedFavoriteToggleReq p1 = new FeedFavoriteToggleReq(1,2);
        given(mapper.insFeedFavorite(p1)).willReturn(1);
        assertEquals(1, service.toggleFavorite(p1), "delFavorite");
        verify(mapper).insFeedFavorite(p1);
    }

    @Test
    void toggleFavoriteDel() {
        FeedFavoriteToggleReq p1 = new FeedFavoriteToggleReq(1,2);

        given(mapper.delFeedFavorite(p1)).willReturn(1);
        assertEquals(0, service.toggleFavorite(p1), "delFavorite");
        verify(mapper).delFeedFavorite(p1);

        FeedFavoriteToggleReq p2 = new FeedFavoriteToggleReq(5,1);

        given(mapper.delFeedFavorite(p2)).willReturn(1);
        assertEquals(0, service.toggleFavorite(p2), "delFavorite");
        verify(mapper).delFeedFavorite(p2);
    }

    @Test
    void toggleFavoritegpt() {
        FeedFavoriteToggleReq p1 = new FeedFavoriteToggleReq(1, 2);

        // delete 상황 테스트
        given(mapper.delFeedFavorite(p1)).willReturn(1);
        assertEquals(0, service.toggleFavorite(p1), "delFavorite");
        verify(mapper).delFeedFavorite(p1);

        // insert 상황 테스트
        given(mapper.delFeedFavorite(p1)).willReturn(0);
        given(mapper.insFeedFavorite(p1)).willReturn(1);
        assertEquals(1, service.toggleFavorite(p1), "insFavorite");
        verify(mapper).insFeedFavorite(p1);
    }
}