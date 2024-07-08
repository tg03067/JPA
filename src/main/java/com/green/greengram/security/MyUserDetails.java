package com.green.greengram.security;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
// 시큐리티에서 로그인 처리를 할 때 사용하는 객체
public class MyUserDetails implements UserDetails, OAuth2User { // 상속관계 is a
    // userDetails = 로그인 처리때, OAuth2User = 소셜로그인 때
    private MyUser user; // JWT 에 만들 떄 payload 에 담을 데이터를 담는 객체 .
    // 가지고 있는 것 has a ( 포함 관계 )

    @Override
    public Map<String, Object> getAttributes() { return Map.of(); }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> list = new ArrayList<>();
//        list.add(new SimpleGrantedAuthority(roles));
//        return list;
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
    }
    @Override public String getPassword() { return "" ; }
    @Override public String getUsername() { return user == null ? "GUEST" : String.valueOf(user.getUserId()) ; }
    @Override public boolean isAccountNonExpired() { return false ; }
    @Override public boolean isAccountNonLocked() { return false ; }
    @Override public boolean isCredentialsNonExpired() { return false ; }
    @Override public boolean isEnabled() { return false ; }
    @Override public String getName() { return "" ; }
}
