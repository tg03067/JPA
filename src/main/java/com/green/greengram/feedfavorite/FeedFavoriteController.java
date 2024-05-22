package com.green.greengram.feedfavorite;

import com.green.greengram.common.model.ResultDto;
import com.green.greengram.feedfavorite.model.FeedFavoriteToggleReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/feed/favorite")
@Tag(name = "좋아요 표시", description = "좋아요를 하기위한 Swagger")
public class FeedFavoriteController {
    private final FeedFavoriteService service;
    // post, del 방식말고 get으로 좋아요처리를 해도되는 이유
    // 전송 데이터 적음, 데이터 노출되면 안되는 것. (post로 처리)
    @GetMapping
    @Operation(summary = "좋아요", description = "Toggle 처리")
    public ResultDto<Integer> toggleFavorite(@ModelAttribute FeedFavoriteToggleReq p){
        int result = service.toggleFavorit(p);
        //result == 0 > 좋아요 취소 (좋아요 > 비좋아요) : 좋아요 취소
        //result == 1 > 좋아요 (비좋아요 > 좋아요) : 좋아요 처리

        return ResultDto.<Integer>builder().
                statusCode(HttpStatus.OK).
                resultMsg(result == 1 ? "좋아요 처리" : "좋아요 취소").
                resultData(result).
                build();
    }
}
