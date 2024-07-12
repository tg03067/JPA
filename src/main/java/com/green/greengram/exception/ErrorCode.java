package com.green.greengram.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name() ; // enum 이 가지고 있는 메소드
    HttpStatus getHttpStatus() ; // httpStatus 멤퍼필드의 getter
    String getMessage() ; // message 멤퍼필드의 getter
}
