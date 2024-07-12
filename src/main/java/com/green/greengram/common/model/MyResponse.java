package com.green.greengram.common.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@SuperBuilder
@Getter
public class MyResponse<T> {
    private HttpStatus statusCode;
    private String resultMsg;
    private T resultData;
}
