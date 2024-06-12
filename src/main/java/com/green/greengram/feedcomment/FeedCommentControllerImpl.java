package com.green.greengram.feedcomment;

import com.green.greengram.common.model.ResultDto;
import com.green.greengram.feedfavorite.FeedFavoriteController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.green.greengram.feedcomment.model.*;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/feed/comment")
@Tag(name = "Feed 댓글", description = "댓글관련")
public class FeedCommentControllerImpl implements FeedCommentController {
    private final FeedCommentService service ;

    @Override
    @PostMapping
    @Operation(summary = "댓글쓰기")
    public ResultDto<Long> postFeedComment(@RequestBody FeedCommentPostReq p){
        log.info("p : {}", p);
        long feedCommentId = service.postFeedComment(p);

        return ResultDto.<Long>builder().
                statusCode(HttpStatus.OK).
                resultMsg(HttpStatus.OK.toString()).
                resultData(feedCommentId).
                build();
    }

    @Override
    @GetMapping
    @Operation(summary = "댓글 가져오기")
    public ResultDto<List<FeedCommentGetRes>> getFeedCommentList(@RequestParam(name = "feed_id") Long feedId) {
        List<FeedCommentGetRes> list = service.getFeedComment(feedId);

        return ResultDto.<List<FeedCommentGetRes>>builder().
                statusCode(HttpStatus.OK).
                resultMsg(String.format("rows: %,d", list.size())).
                resultData(list).
                build();
    }

    @Override
    @DeleteMapping
    @Operation(summary = "댓글지우기")
    public ResultDto<Integer> deleteFeedComment(@ParameterObject @ModelAttribute FeedCommentDeleteReq p){
        int result = service.delFeedComment(p) ; //0은 삭제 실패, 1은 삭제 성공

        return ResultDto.<Integer>builder().
                statusCode(HttpStatus.OK).
                resultMsg(result == 0 ? "댓글 삭제를 할 수 없습니다." : "댓글을 삭제하였습니다.").
                resultData(result).
                build();
    }
}
