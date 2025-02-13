package com.recipe.cookofking.controller;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recipe.cookofking.config.auth.PrincipalDetails;
import com.recipe.cookofking.dto.UserDto;
import com.recipe.cookofking.dto.request.UserRequest;
import com.recipe.cookofking.dto.response.UserResponse;
import com.recipe.cookofking.repository.UserRepository;
import com.recipe.cookofking.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class UserApiController {

//	@Value("${jwt.secretKey}")  // application.properties에서 SECRET_KEY 가져오기
//    private String secretKey;
	
	
	private final UserService userService;
	private final UserRepository userRepository;
	
	
	 
	@GetMapping("/user")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
        Map<String, String> response = new HashMap<>();
        response.put("username", userDetails.getUsername());

        return ResponseEntity.ok(response);
    }
	
	
	
	
	
}

