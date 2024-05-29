package com.green.greengram.userfollow;

import com.green.greengram.userfollow.model.UserFollowReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class) //spring 컨테이너를 사용하고 싶음. 내가 직접
@Import({UserFollowServiceImpl.class}) //객체화할 대상을 작성.
class UserFollowServiceTest {
    @MockBean
    private UserFollowMapper mapper;
    // 가짜 mapper값을 지정해줌 = null 없으면 그냥 mapper를 호출을 못함
    @Autowired
    private UserFollowService service;

    //given - when - then
    @Test
    @DisplayName("유저 servicePost 확인")
    void postUserFollow() {
        UserFollowReq p1 = new UserFollowReq(1, 2);
        UserFollowReq p2 = new UserFollowReq(1, 3);
        UserFollowReq p3 = new UserFollowReq(1, 4);
        //UserFollowReq p4 = new UserFollowReq(1, 2);

        given(mapper.insUserFollow(p1)).willReturn(0);
        given(mapper.insUserFollow(p2)).willReturn(1);
        given(mapper.insUserFollow(p3)).willReturn(2);
        //given(mapper.insUserFollow(any())).willReturn(1); //실행이되면 1을 return


        assertEquals(0, service.postUserFollow(p1),"1");
        assertEquals(1, service.postUserFollow(p2),"2");
        assertEquals(2, service.postUserFollow(p3),"3");
        //assertEquals(1, service.postUserFollow(p4),"4");

        verify(mapper).insUserFollow(p1);
        verify(mapper).insUserFollow(p2);
        verify(mapper).insUserFollow(p3);
        //verify(mapper).insUserFollow(any()); // 정말로 호출했는지 체크
    }

    @Test
    @DisplayName("유저 serviceDelete 확인")
    void deleteUserFollow() {
        UserFollowReq p1 = new UserFollowReq(1, 2);
        UserFollowReq p2 = new UserFollowReq(1, 3);
        UserFollowReq p3 = new UserFollowReq(1, 4);

        given(mapper.delUserFollow(p1)).willReturn(0);
        given(mapper.delUserFollow(p2)).willReturn(1);
        given(mapper.delUserFollow(p3)).willReturn(2);

        assertEquals(0, service.deleteUserFollow(p1),"1");
        assertEquals(1, service.deleteUserFollow(p2),"2");
        assertEquals(2, service.deleteUserFollow(p3),"3");

        verify(mapper).delUserFollow(p1);
        verify(mapper).delUserFollow(p2);
        verify(mapper).delUserFollow(p3);
    }
}