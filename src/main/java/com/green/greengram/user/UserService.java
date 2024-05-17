package com.green.greengram.user;

import com.green.greengram.common.CustomFileUtils;
import com.green.greengram.user.model.SignInPostReq;
import com.green.greengram.user.model.SignInRes;
import com.green.greengram.user.model.SignUpPostReq;
import com.green.greengram.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserMapper mapper;
    private final CustomFileUtils customFileUtils;

    @Transactional
    public int postUser(MultipartFile pic, SignUpPostReq p){
        String saveFileName = customFileUtils.makeRandomFileName(pic);
        p.setPic(saveFileName);
        String hashPassword = BCrypt.hashpw(p.getUpw(), BCrypt.gensalt());
        p.setUpw(hashPassword);

        int result = mapper.postUser(p);
        if(pic == null){
            return result ;
        }
        try {
            String path = String.format("user/%d", p.getUserId());
            customFileUtils.makeFolders(path);
            String target = String.format("%s/%s",path,saveFileName);
            customFileUtils.transferTo(pic,target);
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("파일오류");
        }
        return result ;
    }

    public SignInRes getUserById(SignInPostReq p){
        User user = mapper.getUserById(p.getUid());
        if(Objects.isNull(user)){
            throw new RuntimeException("아이디를 확인해 주세요.");
        } else if(!BCrypt.checkpw(p.getUpw(), user.getUpw())){
            throw new RuntimeException("비밀번호를 확인해 주세요");
        }

        return SignInRes.builder().
                userId(user.getUserId()).
                nm(user.getNm()).
                pic(user.getPic()).
                build();
    }
}
