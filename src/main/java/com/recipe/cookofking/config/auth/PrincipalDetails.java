package com.recipe.cookofking.config.auth;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.recipe.cookofking.dto.UserDto;
import com.recipe.cookofking.entity.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {

    private static final long serialVersionUID = 1L;
    
    
	private final User user;  // User 엔티티를 직접 사용
    private final UserDto userDto;

    public PrincipalDetails(User user) {
    	this.user=user;
    	this.userDto=UserDto.fromEntity(user);
    }


    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	// user.getRoleType()이 null일 경우 기본값 설정
        if (user.getRole() == null) {
            return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));  // 기본값으로 ROLE_USER 사용
        }
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();  // user 객체에서 password 가져오기
    }

    @Override
    public String getUsername() {
        return user.getUsername();  // user 객체에서 uid 가져오기
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	
}