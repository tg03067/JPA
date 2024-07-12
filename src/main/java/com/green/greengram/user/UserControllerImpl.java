package com.green.greengram.user;

import com.green.greengram.common.model.MyResponse;
import com.green.greengram.user.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Tag(name = "유저 컨트롤러", description = "유저 CRUD, sign-up, sign-out")
public class UserControllerImpl {
    private final UserServiceImpl service;

    @PostMapping("sign-up")
    @Operation(summary = "회원가입", description = "프로필 사진은 필수가 아닙니다.")
    public MyResponse<Integer> postUser(@RequestPart(required = false) MultipartFile pic,
                                        @RequestPart SignUpPostReq p){
        log.info("pic: {}",pic);
        log.info("p: {}",p);
        int result = service.postUser(pic, p);

        return MyResponse.<Integer>builder().
                statusCode(HttpStatus.OK).
                resultMsg("회원가입 성공.").
                resultData(result).build();
    }

    @PostMapping("sign-in")
    @Operation(summary = "로그인")
    public MyResponse<SignInRes> postSignIn(@Valid @RequestBody SignInPostReq p, HttpServletResponse res){
        log.info("p; {}", p);
        SignInRes result = service.getUserById(p, res);

        return MyResponse.<SignInRes>builder().
                statusCode(HttpStatus.OK).
                resultMsg("인증 성공").
                resultData(result).
                build();
    }
    /*
         FE는 단지 get 방식으로 아무런 작업었이 요청만하면 refresh-token 이 넘어온다.
         이유는 우리가 refresh-token 을 로그인을 성공하면 cookie 에 담았기 때문
         cookie 는 요청마다 항상 넘어온다.
     */
    @GetMapping("access-token")
    public MyResponse<Map<String, String>> getAccessToken(HttpServletRequest req) {
        Map<String, String> map = service.getAccessToken(req);

        return MyResponse.<Map<String, String>>builder().
                statusCode(HttpStatus.OK).
                resultMsg("Access Token 발급").
                resultData(map).
                build();
    }

    @GetMapping
    @Operation(summary = "팔로워 확인", description = "팔로우, 팔로워, 상태 확인 가능.")
    public MyResponse<UserInfoGetRes> getUserInfo(@ParameterObject @ModelAttribute UserInfoGetReq p){
        UserInfoGetRes result = service.getUserInfo(p);

        return MyResponse.<UserInfoGetRes>builder().
                statusCode(HttpStatus.OK).
                resultMsg(HttpStatus.OK.toString()).
                resultData(result).
                build();
    }

    @PatchMapping(value = "pic")
    @Operation(summary = "프로필 사진 변경", description = "프로필 사진 변경 가능")
    public MyResponse<String> patchProfilePic(@ModelAttribute UserProfilePatchReq p) {
        String result = service.patchProfilePic(p);

        return MyResponse.<String>builder().
                statusCode(HttpStatus.OK).
                resultMsg(HttpStatus.OK.toString()).
                resultData(result).
                build();
    }
}