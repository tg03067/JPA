package com.green.greengram.userfollow;

import com.green.greengram.common.model.MyResponse;
import com.green.greengram.userfollow.model.UserFollowDeleteReq;
import com.green.greengram.userfollow.model.UserFollowPostReq;
import com.green.greengram.userfollow.model.UserFollowReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/user/follow")
@Tag(name = "유저 Follow", description = "유저 Follow CRUD")
public class UserFollowControllerImpl implements UserFollowController {
    private final UserFollowService service;

    @Override
    @PostMapping
    @Operation(summary = "유저간 Follow", description = "Follow 처리")
    public MyResponse<Integer> postUserFollow(@RequestBody UserFollowPostReq p) {
        int result = service.postUserFollow(p);

        return MyResponse.<Integer>builder().
                statusCode(HttpStatus.OK).
                resultMsg(HttpStatus.OK.toString()).
                resultData(result).
                build();
    }

    @Operation(summary = "유저간 Follow 취소", description = "Follow 취소 처리")
    @Override
    @DeleteMapping
    public MyResponse<Integer> deleteUserFollow(@ParameterObject @ModelAttribute UserFollowDeleteReq p) {
        int result = service.deleteUserFollow(p);

        return MyResponse.<Integer>builder()
                .statusCode(HttpStatus.OK)
                .resultMsg(HttpStatus.OK.toString())
                .resultData(result)
                .build();
    }
}
