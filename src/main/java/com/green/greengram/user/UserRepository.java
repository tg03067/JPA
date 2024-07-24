package com.green.greengram.user;

import com.green.greengram.entity.User;
import com.green.greengram.security.SignInProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
//                                                  Entity, PK 타입
public interface UserRepository extends JpaRepository<User, Long> {
    // 쿼리 메소드 : by 뒤에 넣고싶은 where 절넣으면 자동으로 만들어줌 and 도 가능
    // getUserByUidAndUpw - get 대신 find 도 가능 + entity 명 + by + 변수명
    // and 자리에 orderby 등도 사용가능
    User getUserByProviderTypeAndUid(SignInProviderType providerType, String uid) ;
    // select * from user where provider_type = #{providerType} AND uid = #{uid} 위와 같음
    // enum 타입도 가능


}
