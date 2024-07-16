package com.green.greengram.admin;

import com.green.greengram.admin.model.GetAdminProviderRes;
import com.green.greengram.common.model.MyResponse;
import com.green.greengram.security.jwt.JwtTokenProviderV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "관리자 권한", description = "ADMIN CRUD")
public class AdminControllerImpl {
    private final AdminServiceImpl service ;
    private final JwtTokenProviderV2 tokenProvider ;

    // (get) api/admin/provider-count
    /*
    [
        {
            "provider":"LOCAL",
            "count":5
        },
        {
            "provider":"GOOGLE",
            "count":2
        },
        {
            "provider":"KAKAO",
            "count":3
        },
        {
            "provider":"NAVER",
            "count":1
        }
    ]
     */
    @GetMapping("/provider-count") @Operation(summary = "로컬회원 조회")
    public MyResponse<List<GetAdminProviderRes>> getAdminProvider(HttpServletRequest req) {
        String token = tokenProvider.resolveToken(req) ;
        if (token == null) {
            throw new RuntimeException("접근할 수 없습니다.") ;
        }
        List<GetAdminProviderRes> res = service.getAdminProvider(token) ;

        return MyResponse.<List<GetAdminProviderRes>>builder()
                .statusCode(HttpStatus.OK)
                .resultMsg(HttpStatus.OK.toString())
                .resultData(res)
                .build() ;
    }
}
