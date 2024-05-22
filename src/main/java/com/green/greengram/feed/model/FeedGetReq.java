package com.green.greengram.feed.model;

import com.green.greengram.common.GlobalConst;
import com.green.greengram.common.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.bind.annotation.BindParam;

@Getter
@Setter
public class FeedGetReq extends Paging {
    @Schema(name = "signed_user_id")
    private long signedUserId;
    @Schema(name = "profile_user_id", description = "프로필 사용자 ID (Not Required), 프로필 화면에서 사용")
    private Long profileUserId;

    public FeedGetReq(Integer page, Integer size,
                      @BindParam("signed_user_id") long signedUserId,
                      @BindParam("profile_user_id") Long profileUserId){
        super(page, size == null || size == 0 ? GlobalConst.FEED_PAGING_SIZE : size );
        this.signedUserId = signedUserId;
        this.profileUserId = profileUserId;
    }
}
