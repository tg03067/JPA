package com.green.greengram.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Paging {
    @Schema(example = "1", description = "게시글 페이지 수")
    private Integer page ;
    @Schema(example = "10", description = "게시글 수")
    private Integer size ;

    public Paging(Integer page, Integer size){
        this.page = page == null || page == 0 ? 1 : page ;
        this.size = size == null || size == 0 ? 10 : size ;
        this.startIdx = this.page - 1 < 0 ? 0 : (this.page - 1) * this.size ;
    }
    @JsonIgnore
    private int startIdx;
}
