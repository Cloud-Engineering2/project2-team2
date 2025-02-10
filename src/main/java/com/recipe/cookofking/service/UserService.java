package com.recipe.cookofking.service;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.recipe.cookofking.config.jwt.JwtProperties;
import com.recipe.cookofking.dto.UserDto;
import com.recipe.cookofking.entity.User;
import com.recipe.cookofking.repository.UserRepository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	
	// 사용자 인증 후 JWT 토큰 발급
    public String authenticateUser(UserDto userDto) throws Exception {
        // 인증 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken = 
            new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());

        // 인증 처리
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 인증 성공 후 JWT 토큰 생성
        String jwtToken = JWT.create()
            .withSubject(authentication.getName())  // 사용자 이름
            .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))  // 만료 시간
            .withClaim("uid", userDto.getUsername())  // 추가적인 claim, 예: 사용자 ID
            .sign(Algorithm.HMAC512(JwtProperties.SECRET));  // 비밀 키로 서명

        return jwtToken;
    }
    
    
	public void registerUser(UserDto userDto) {
		
		// 아이디 중복체크 
		if(userRepository.existsByUsername(userDto.getUsername())) {
			throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
		}
		// 암호화 추가
		String rawPassword = userDto.getPassword();
		String encryptedPassword = passwordEncoder.encode(rawPassword);
		userDto.setPassword(encryptedPassword);
		
		//userDto를 user 엔티티로 변환
		User user = userDto.toEntity();
		userRepository.save(user);
		
	}
	
	
	

}