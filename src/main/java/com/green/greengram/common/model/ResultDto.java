package com.green.greengram.common.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Builder
@Data
public class ResultDto<T> {
    private HttpStatus httpStatus;
    private String resultMsg;
    private T resultData;
}
