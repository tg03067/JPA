package com.green.greengram.feedfavorite;

import com.green.greengram.feedfavorite.model.FeedFavoriteToggleReq;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Import({FeedFavoriteServiceImpl.class})
class FeedFavoriteServiceTest {

    @MockBean
    private FeedFavoriteMapper mapper;
    @Autowired
    private FeedFavoriteServiceImpl service;

    @Test
    void toggleFavoriteTe() {
        FeedFavoriteToggleReq p1 = new FeedFavoriteToggleReq(1,2);
        FeedFavoriteToggleReq p2 = new FeedFavoriteToggleReq(10,20);

        given(mapper.delFeedFavorite(p1)).willReturn(0);
        given(mapper.delFeedFavorite(p2)).willReturn(1);

        given(mapper.insFeedFavorite(p1)).willReturn(100);
        given(mapper.insFeedFavorite(p2)).willReturn(200);

        int result1 = service.toggleFavorite(p1);
        assertEquals(100, result1);
        int result2 = service.toggleFavorite(p2);
        assertEquals(0, result2);

        verify(mapper, times(1)).delFeedFavorite(p1);
        verify(mapper, times(1)).delFeedFavorite(p2);
        verify(mapper, times(1)).insFeedFavorite(p1);
        verify(mapper, never()).insFeedFavorite(p2);
    }

    @Test
    void toggleFavoriteIns() {
        FeedFavoriteToggleReq p1 = new FeedFavoriteToggleReq(1,2);
        given(mapper.insFeedFavorite(p1)).willReturn(1);
        assertEquals(1, service.toggleFavorite(p1), "insFavorite");
        verify(mapper).insFeedFavorite(p1);
    }

    @Test
    void toggleFavoriteDel() {
        FeedFavoriteToggleReq p1 = new FeedFavoriteToggleReq(1,2);

        given(mapper.delFeedFavorite(p1)).willReturn(1);
        assertEquals(0, service.toggleFavorite(p1), "delFavorite");
        verify(mapper).delFeedFavorite(p1);

        FeedFavoriteToggleReq p2 = new FeedFavoriteToggleReq(5,1);

        given(mapper.delFeedFavorite(p2)).willReturn(0);
        assertEquals(0, service.toggleFavorite(p2), "delFavorite");
        verify(mapper).delFeedFavorite(p2);

        FeedFavoriteToggleReq p3 = new FeedFavoriteToggleReq(1,3);

        given(mapper.delFeedFavorite(p3)).willReturn(1);
        assertEquals(0, service.toggleFavorite(p3), "delFavorite");
        verify(mapper).delFeedFavorite(p3);
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