package com.recipe.cookofking.service;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
//    public String authenticateUser(UserDto userDto) throws Exception {
//        // 인증 토큰 생성
//        UsernamePasswordAuthenticationToken authenticationToken = 
//            new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());
//
//        // 인증 처리
//        Authentication authentication = authenticationManager.authenticate(authenticationToken);
//
//        // 인증 성공 후 JWT 토큰 생성
//        String jwtToken = JWT.create()
//            .withSubject(authentication.getName())  // 사용자 이름
//            .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))  // 만료 시간
//            .withClaim("username", userDto.getUsername())  // 추가적인 claim, 예: 사용자 ID
//            .sign(Algorithm.HMAC512(JwtProperties.SECRET));  // 비밀 키로 서명
//
//        return jwtToken;
//    }
    
    
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
    

    
	// 로그인 처리
	public UserDto login(String username, String password) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return UserDto.fromEntity(user);
    }	
	
	// 이메일 업데이트 메서드, DTO를 사용하여 이메일 수정
	public void updateUserEmail(UserDto userDto) {
        // username으로 사용자 정보 조회
        User existingUser = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // UserDto를 사용하여 엔티티 수정
        User updatedUser = userDto.toUpdatedEntity(existingUser);

        // 수정된 사용자 정보 저장
        userRepository.save(updatedUser);
    }
	
	
	public User findByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(()-> new UsernameNotFoundException("user"));
	}
	public UserDto findUserByUsername(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
		return UserDto.fromEntity(user);
	}
	
	
	public boolean changePassword(String username, String currentPassword, String newPassword) {
	    Optional<User> optionalUser = userRepository.findByUsername(username);

	    if (optionalUser.isPresent()) {
	        User existingUser = optionalUser.get();

	        // 현재 비밀번호 확인
	        if (passwordEncoder.matches(currentPassword, existingUser.getPassword())) {
	            User updatedUser = User.builder()
	                .id(existingUser.getId())
	                .username(existingUser.getUsername())
	                .email(existingUser.getEmail())
	                .password(passwordEncoder.encode(newPassword))
	                .role(existingUser.getRole())
	                .createdDate(existingUser.getCreatedDate())
	                .modifiedDate(LocalDateTime.now())
	                .build();

	            userRepository.save(updatedUser);
	            return true;
	        }
	    }

	    return false;
	}




}