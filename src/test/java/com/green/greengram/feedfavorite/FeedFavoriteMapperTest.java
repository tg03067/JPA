package com.green.greengram.feedfavorite;

import com.green.greengram.feedfavorite.model.FeedFavoriteEntity;
import com.green.greengram.feedfavorite.model.FeedFavoriteToggleReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@MybatisTest
@ActiveProfiles("tdd")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FeedFavoriteMapperTest {

    @Autowired
    private FeedFavoriteMapper mapper;
    private final int RECORD_COUNT = 10;

    @Test
    @DisplayName("팔로우 insert 테스트")
    void insFeedFavorite() {
        FeedFavoriteToggleReq p1 = new FeedFavoriteToggleReq(0);
        List<FeedFavoriteEntity> list1 = mapper.selFeedFavoriteForTest(p1);
        assertEquals(RECORD_COUNT, list1.size(), "0. 먼가이상");

        FeedFavoriteToggleReq p2 = new FeedFavoriteToggleReq(1);
        int result = mapper.insFeedFavorite(p2);
        assertEquals(1, result, "1. 먼가이상");
        List<FeedFavoriteEntity> list2 = mapper.selFeedFavoriteForTest(p1);
        assertEquals(1, list2.size() - list1.size(), "2. 먼가이상");

        FeedFavoriteToggleReq p3 = new FeedFavoriteToggleReq(1);
        int result2 = mapper.insFeedFavorite(p3);
        assertEquals(1, result2, "3. 먼가이상");
        List<FeedFavoriteEntity> list3 = mapper.selFeedFavoriteForTest(p1);
        assertEquals(1, list3.size() - list2.size(), "4. 먼가이상");
        List<FeedFavoriteEntity> list4 = mapper.selFeedFavoriteForTest(p3);
        assertEquals(1, list4.size(), "5. 먼가이상");
    }

    @Test
    @DisplayName("팔로우 select 테스트")
    void selFeedFavoriteForTest() {
        FeedFavoriteToggleReq p1 = new FeedFavoriteToggleReq( 0);
        List<FeedFavoriteEntity> list1 = mapper.selFeedFavoriteForTest(p1);
        assertEquals(RECORD_COUNT, list1.size(), "1. 먼가이상");

        FeedFavoriteToggleReq p2 = new FeedFavoriteToggleReq(1);
        List<FeedFavoriteEntity> list2 = mapper.selFeedFavoriteForTest(p2);
        assertEquals(1, list2.size(), "2. 먼가이상");

        FeedFavoriteToggleReq p3 = new FeedFavoriteToggleReq(12);
        List<FeedFavoriteEntity> list3 = mapper.selFeedFavoriteForTest(p3);
        assertEquals(0, list3.size(), "3. 먼가이상");

        FeedFavoriteToggleReq p4 = new FeedFavoriteToggleReq(1);
        List<FeedFavoriteEntity> list4 = mapper.selFeedFavoriteForTest(p4);
        assertEquals(2, list4.size(), "4. 먼가이상");
        assertEquals(new FeedFavoriteEntity(5, 1, "2024-05-09 11:51:47"),
                list1.get(0), "5. 먼가이상");

        FeedFavoriteToggleReq p5 = new FeedFavoriteToggleReq( 0);
        List<FeedFavoriteEntity> list5 = mapper.selFeedFavoriteForTest(p5);
        assertEquals(1, list5.size(), "6. 먼가이상");
    }

    @Test
    @DisplayName("팔로우 delete 테스트")
    void delFeedFavorite() {
        FeedFavoriteToggleReq p1 = new FeedFavoriteToggleReq(0);
        List<FeedFavoriteEntity> list1 = mapper.selFeedFavoriteForTest(p1);

        FeedFavoriteToggleReq p2 = new FeedFavoriteToggleReq(5);
        int del = mapper.delFeedFavorite(p2);
        assertEquals(0, del,"1. 먼가이상");

        FeedFavoriteToggleReq p3 = new FeedFavoriteToggleReq(5);
        int del2 = mapper.delFeedFavorite(p3);
        assertEquals(1, del2, "2. 먼가이상");
        List<FeedFavoriteEntity> list2 = mapper.selFeedFavoriteForTest(p1);
        assertEquals(1, list1.size() - list2.size(), "3. 먼가이상");
    }
}