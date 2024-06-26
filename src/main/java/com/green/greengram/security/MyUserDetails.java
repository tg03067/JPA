package com.green.greengram.security;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@NoArgsConstructor
public class MyUserDetails implements UserDetails {
    private MyUser user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> list = new ArrayList<>();
//        list.add(new SimpleGrantedAuthority(roles));
//        return list;
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override public String getPassword() {return "";}
    @Override public String getUsername() {return "";}
    @Override public boolean isAccountNonExpired() {return false;}
    @Override public boolean isAccountNonLocked() {return false;}
    @Override public boolean isCredentialsNonExpired() {return false;}
    @Override public boolean isEnabled() {return false;}
}
