package com.green.greengram.feedcomment;

import com.green.greengram.common.model.MyResponse;
import com.green.greengram.entity.FeedComment;
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

    /*
        HttpServletRequest( 이하 req ), HttpServletResponse ( 이하 res ), Client ( 서버에게 요청을 보내는 EndUser )
        req : 요청에 관련한 모든 정보가 담겨져있는 객체 ( Client 의 IP 주소, 사용하는 브라우저 엔진, OS, URL, 쿼리스트링, Body, Header 어떤 데이터가 담겨져 있는지 등등 )
        res : 서버가 응답을 할 때 사용할 객체
     */

    @Override @PostMapping @Operation(summary = "댓글쓰기")
    public MyResponse<Long> postFeedComment(@RequestBody FeedCommentPostReq p){
        log.info("p : {}", p);
        long feedCommentId = service.postFeedComment(p);

        return MyResponse.<Long>builder().
                statusCode(HttpStatus.OK).
                resultMsg(HttpStatus.OK.toString()).
                resultData(feedCommentId).
                build();
    }

    @Override @GetMapping @Operation(summary = "댓글 가져오기")
    public MyResponse<List<FeedCommentGetResInterpace>> getFeedCommentList(@RequestParam(name = "feed_id") Long feedId) {
        List<FeedCommentGetResInterpace> list = service.getFeedComment(feedId);

        return MyResponse.<List<FeedCommentGetResInterpace>>builder().
                statusCode(HttpStatus.OK).
                resultMsg(String.format("rows: %,d", list.size())).
                resultData(list).
                build();
    }

    @Override @DeleteMapping @Operation(summary = "댓글지우기")
    public MyResponse<Integer> deleteFeedComment(@ParameterObject @ModelAttribute FeedCommentDeleteReq p){
        int result = service.delFeedComment(p) ; //0은 삭제 실패, 1은 삭제 성공

        return MyResponse.<Integer>builder().
                statusCode(HttpStatus.OK).
                resultMsg(result == 0 ? "댓글 삭제를 할 수 없습니다." : "댓글을 삭제하였습니다.").
                resultData(result).
                build();
    }
}
