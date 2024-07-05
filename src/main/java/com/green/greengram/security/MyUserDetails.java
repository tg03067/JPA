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
public class MyUserDetails implements UserDetails, OAuth2User {
    private MyUser user;

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
