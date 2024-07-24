package com.green.greengram.user;

import com.green.greengram.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    // 쿼리 메소드 select * from user_role where user_id = #{userId}
    // by 뒤에는 컬럼명으로 작성
    List<UserRole> findAllByUserId(Long userId) ;
}
