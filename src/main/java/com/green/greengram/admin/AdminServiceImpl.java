package com.green.greengram.admin;

import com.green.greengram.admin.model.GetAdminProviderRes;
import com.green.greengram.admin.model.GetAdminReq;
import com.green.greengram.security.MyUserDetails;
import com.green.greengram.security.jwt.JwtTokenProviderV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl {
    private final AdminMapper mapper ;
    private final JwtTokenProviderV2 tokenProvider ;

    public List<GetAdminProviderRes> getAdminProvider(String token) {
        Authentication auth = tokenProvider.getAuthentication(token) ;
        SecurityContextHolder.getContext().setAuthentication(auth) ;
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal() ;
        long userId = userDetails.getUser().getUserId() ;
        GetAdminReq req = new GetAdminReq() ;
        req.setSignedUserId(userId) ;
        return mapper.getAdminProvider(req);
    }
}
