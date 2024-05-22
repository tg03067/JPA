package com.green.greengram.user;

import com.green.greengram.common.model.ResultDto;
import com.green.greengram.user.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Tag(name = "유저 컨트롤러", description = "유저 CRUD, sign-up, sign-out")
public class UserController {
    private final UserService service;

    @PostMapping("sign-up")
    @Operation(summary = "회원가입", description = "프로필 사진은 필수가 아닙니다.")
    public ResultDto<Integer> postUser(@RequestPart(required = false) MultipartFile pic,
                                        @RequestPart SignUpPostReq p){
        log.info("pic: {}",pic);
        log.info("p: {}",p);
        int result = service.postUser(pic, p);

        return ResultDto.<Integer>builder().
                statusCode(HttpStatus.OK).
                resultMsg("회원가입 성공.").
                resultData(result).build();
    }

    @PostMapping("sign-in")
    @Operation(summary = "로그인")
    public ResultDto<SignInRes> postSignIn(@RequestBody SignInPostReq p){
        log.info("p; {}", p);
        SignInRes result = service.getUserById(p);

        return ResultDto.<SignInRes>builder().
                statusCode(HttpStatus.OK).
                resultMsg("인증 성공").
                resultData(result).
                build();
    }
    @GetMapping
    @Operation(summary = "팔로워 확인", description = "팔로우, 팔로워, 상태 확인 가능.")
    public ResultDto<UserInfoGetRes> getUserInfo(@ParameterObject @ModelAttribute UserInfoGetReq p){
        UserInfoGetRes result = service.getUserInfo(p);

        return ResultDto.<UserInfoGetRes>builder().
                statusCode(HttpStatus.OK).
                resultMsg(HttpStatus.OK.toString()).
                resultData(result).
                build();
    }
    @PatchMapping("pic")
    @Operation(summary = "프로필 사진 변경", description = "프로필 사진 변경 가능")
    public ResultDto<String> patchProfilePic(@RequestPart UserProfilePatchReq p) {
        String result = service.patchProfilePic(p);

        return ResultDto.<String>builder().
                statusCode(HttpStatus.OK).
                resultMsg(HttpStatus.OK.toString()).
                resultData(result).
                build();
    }
}