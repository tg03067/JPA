package com.green.greengram.userfollow.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
//모든 객체는 Object를 상속하기 때문에 Equals, HashCode 사용가능.
//Overring - 부모객체가 가지고 있는 값을 재정의함
@AllArgsConstructor
@ToString
//Overring - 부모객체가 가지고 있는 값을 재정의함
public class UserFollowEntity {
    private long fromUserId;
    private long toUserId;
    private String createdAt;
}
