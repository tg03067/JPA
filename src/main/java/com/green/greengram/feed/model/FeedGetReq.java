package com.green.greengram.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.greengram.common.GlobalConst;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FeedGetReq {
    @Schema(defaultValue = "1")
    private int page;
    private long loginedUserId;

    @JsonIgnore
    private int startIdx;
    @JsonIgnore
    private int len;

    public void setPage(int page){
        this.page = page ;
        this.len = GlobalConst.FEED_PAGE_ITEM_SIZE ;
        this.startIdx = ( this.page - 1 ) < 0 ? 1 : ( this.page - 1 ) * this.len ;
    }
}
