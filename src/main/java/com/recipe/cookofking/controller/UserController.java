package com.recipe.cookofking.controller;



import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.recipe.cookofking.dto.UserDto;
import com.recipe.cookofking.dto.request.UserRequest;
import com.recipe.cookofking.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {
	
	private final UserService userService;

	@GetMapping("/mypage")
	public String myPage() {
		return "user/mypage";
	}

	
	/* 로그인 페이지 */
	@GetMapping("/login")
	public String login( ) {
		 log.info("로그인 페이지 !");
		
	    return "user/login"; // 로그인 페이지로 이동
	}
	
	/* 로그인 기능 */
	@PostMapping("/login")
	@ResponseBody
	public ResponseEntity<Map<String, String>> login(@RequestBody UserRequest userRequest) {
	    log.info("로그인 시도: {}", userRequest.getUsername());

	    try {
	        // UserRequest에서 UserDto로 변환
	        UserDto userDto = userRequest.toDto();

	        
	        // 사용자 인증 및 JWT 토큰 발급
	        String token = userService.authenticateUser(userDto);  // JWT 토큰 발급

	        // 로그인 성공 시 메시지 및 토큰 반환
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "로그인 성공!");
	        response.put("token", token);  // 토큰을 응답 본문에 포함시킴

	        // 200 OK 응답, 헤더에 Authorization 포함
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", "Bearer " + token);  // 헤더에 토큰 추가

	        return ResponseEntity.ok()
	                             .headers(headers)
	                             .body(response);  // 응답 본문과 헤더 모두 반환

	    } catch (Exception e) {
	        // 로그인 실패 시 메시지 반환
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "로그인 실패: " + e.getMessage());
	        
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)  // 401 Unauthorized
	                             .body(response);
	    }
	}
	
	
	/* 회원가입 페이지 이동*/
	@GetMapping("/register")
	public String userregister() {
		return "/user/register";
	}
	
	
	/* 회원가입 기능 */
	@PostMapping("/register")
	public String signup(@ModelAttribute UserRequest userRequest, RedirectAttributes redirectAttributes) {
		try {
		
			// UserRequest를 UserDto로 변환
			UserDto userDto= userRequest.toDto();
			
			userService.registerUser(userDto);
			log.info("회원가입에서 입력한 값 : " + userRequest); // 로그 추가
			log.info("디비에저장되는값 : " + userDto); // 로그 추가
			
			//회원가입 성공 메시지 전달
			redirectAttributes.addFlashAttribute("successMessage", "회원가입이 완료되었습니다. 로그인해주세요.");
			return "redirect:/user/login";
			
		}catch(IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());  // 중복 아이디 메시지
	        return "/user/register";  // 회원가입 페이지로 돌아감
		}catch(Exception e) {
			 System.out.println("회원가입 오류: " + 	e.getMessage()); 
			 redirectAttributes.addFlashAttribute("errorMessage", "회원가입에 실패했습니다: " + e.getMessage());
		        return "/user/register"; // 오류 발생 시 회원가입 페이지로 복귀
		}
	}
	
	
	
}
