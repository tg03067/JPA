package com.green.greengram.user.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class UserInfoGetRes {
    private String nm ;
    private String pic ;
    private String createdAt ;
    private int feedCnt ;
    private int favCnt ;
    private int following ;
    private int follower ;
    private int followState ;
}
