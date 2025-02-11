package com.recipe.cookofking.controller;



import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.recipe.cookofking.dto.post.PostDto;
import com.recipe.cookofking.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.recipe.cookofking.config.auth.PrincipalDetails;
import com.recipe.cookofking.dto.UserDto;
import com.recipe.cookofking.dto.request.ChangePasswordRequest;
import com.recipe.cookofking.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {
	
	private final UserService userService;
	private final PostService postService;

	
	@GetMapping("/mypage/checkSession")
	public ResponseEntity<?> checkSession() {
	    // 현재 인증된 사용자 정보 확인
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication == null || !authentication.isAuthenticated()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
	    }
	    return ResponseEntity.ok("로그인 상태입니다.");
	}
	
	@GetMapping("/session-info")
	public String getSessionInfo(HttpServletRequest request) {
	    HttpSession session = request.getSession(false); // 세션이 없으면 null 반환
	    if (session == null) {
	        return "세션이 없습니다.";
	    }

	    SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
	    if (securityContext == null || securityContext.getAuthentication() == null) {
	        return "로그인되지 않은 사용자입니다.";
	    }

	    Authentication authentication = securityContext.getAuthentication();
	    return "로그인된 사용자: " + authentication.getName();
	}

	
	@GetMapping("/mypage")
	public String getUserDetail(Authentication authentication, Model model) {
	    if (authentication == null) {
	        return "redirect:/user/login"; // 로그인 페이지로 리다이렉트
	    }

	    PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();

	    model.addAttribute("username", userDetails.getUsername());
	    model.addAttribute("email", userDetails.getUserDto().getEmail());

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	    String formattedDate = userDetails.getUserDto().getCreatedDate().format(formatter);
	    model.addAttribute("createdDate", formattedDate);

	    return "user/mypage"; // Thymeleaf 템플릿 이름
	}

	
	// 이메일 변경
	@PutMapping("/mypage/update")
	public ResponseEntity<?> updateUserEmail(@RequestBody UserDto userDto, Authentication authentication) {
	    if (authentication == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
	    }

	    PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
	    
	    userDto.setUsername(userDetails.getUsername());

	    // 이메일 값이 null이거나 비어있는지 확인
	    if (userDto.getEmail() == null || userDto.getEmail().trim().isEmpty()) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일을 입력해 주세요.");
	    }

	    try {
	        userService.updateUserEmail(userDto);
	        return ResponseEntity.ok("이메일이 성공적으로 업데이트되었습니다.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 업데이트 실패: " + e.getMessage());
	    }
	}
	
	
	@PutMapping("/mypage/changePassword")
    @ResponseBody
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        // 비밀번호 변경 로직 수행
        boolean isPasswordChanged = userService.changePassword(changePasswordRequest.getUsername(),
                                                              changePasswordRequest.getCurrentPassword(),
                                                              changePasswordRequest.getNewPassword());

        if (isPasswordChanged) {
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("현재 비밀번호가 일치하지 않거나 다른 오류가 발생했습니다.");
        }
    }


	/* 로그인 페이지 */
	@GetMapping("/myrecipe")
	public String getMyPostList(Authentication authentication, Model model,
							  @RequestParam(defaultValue = "1") int page,
							  @RequestParam(defaultValue = "20") int size,
							  @RequestParam(defaultValue = "latest") String sort) {


		if (authentication == null) {
			return "redirect:/user/login"; // 로그인 페이지로 리다이렉트
		}

		PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
		Integer userId = userDetails.getUserDto().getId();


		// 페이지 번호를 0부터 시작하도록 조정
		int adjustedPage = (page > 0) ? page - 1 : 0;

		// 정렬 기준 설정
		Sort sortOrder = getSortOrder(sort);
		PageRequest pageRequest = PageRequest.of(adjustedPage, size, sortOrder);

		// 서비스 호출
		Page<PostDto> postPage = postService.getMyPostList(pageRequest, userId);


		// 모델에 데이터 추가
		model.addAttribute("postPage", postPage);
		model.addAttribute("totalPosts", postPage.getTotalElements());
		model.addAttribute("currentSort", sort);
		model.addAttribute("currentPage", page);  // 현재 페이지를 모델에 추가 (1부터 시작)

		return "user/myrecipe";  // posts.html로 렌더링
	}

	// 정렬 기준에 따라 Sort 객체 반환
	private Sort getSortOrder(String sort) {
		switch (sort) {
			case "views":
				return Sort.by(Sort.Direction.DESC, "viewCount");  // 조회수 기준 정렬
			case "likes":
				return Sort.by(Sort.Direction.DESC, "likeCount");  // 좋아요 기준 정렬
			default:
				return Sort.by(Sort.Direction.DESC, "createdDate");  // 기본값: 최신순 정렬
		}
	}

	
	/* 로그인 페이지 */
	@GetMapping("/login")
	public String login( ) {
		 log.info("로그인 페이지 !");
		
	    return "user/login"; // 로그인 페이지로 이동
	}


//	@PostMapping("/login")
//	public String login(@RequestParam String username, 
//	                    @RequestParam String password, 
//	                    HttpServletRequest request, 
//	                    RedirectAttributes redirectAttributes) {
//	    try {
//	        UserDto userDto = userService.login(username, password);
//
//	        // ✅ 직접 세션에 유저 정보 저장
//	        HttpSession session = request.getSession();
//	        session.setAttribute("user", userDto);
//
//	        return "redirect:/user/mypage"; // 로그인 성공 시 마이페이지로 이동
//	    } catch (Exception e) {
//	        redirectAttributes.addFlashAttribute("error", "Invalid username or password");
//	        return "redirect:/user/login"; // 로그인 실패 시 다시 로그인 페이지로
//	    }
//	}
	
	 @PostMapping("/user/logout")
	    public String logout(HttpServletRequest request) {
	        HttpSession session = request.getSession(false);
	        if (session != null) {
	            session.invalidate(); // 세션 무효화
	        }
	        return "redirect:/user/login";
	    }
	 

	
	
	/* 회원가입 페이지 이동*/
	@GetMapping("/register")
	public String userregister() {
		return "user/register";
	}
	
	@PostMapping("/register")
	public String register(@ModelAttribute UserDto userDto) {
		userService.registerUser(userDto);
		return "redirect:/user/login";
	}
	
	
	
	
	
//	/* 로그인 기능 */
//	@PostMapping("/login")
//	@ResponseBody
//	public ResponseEntity<Map<String, String>> login(@RequestBody UserRequest userRequest) {
//	    log.info("로그인 시도: {}", userRequest.getUsername());
//
//	    try {
//	        // UserRequest에서 UserDto로 변환
//	        UserDto userDto = userRequest.toDto();
//
//	        
//	        // 사용자 인증 및 JWT 토큰 발급
//	        String token = userService.authenticateUser(userDto);  // JWT 토큰 발급
//
//	        // 로그인 성공 시 메시지 및 토큰 반환
//	        Map<String, String> response = new HashMap<>();
//	        response.put("message", "로그인 성공!");
//	        response.put("token", token);  // 토큰을 응답 본문에 포함시킴
//
//	        // 200 OK 응답, 헤더에 Authorization 포함
//	        HttpHeaders headers = new HttpHeaders();
//	        headers.set("Authorization", "Bearer " + token);  // 헤더에 토큰 추가
//
//	        return ResponseEntity.ok()
//	                             .headers(headers)
//	                             .body(response);  // 응답 본문과 헤더 모두 반환
//
//	    } catch (Exception e) {
//	        // 로그인 실패 시 메시지 반환
//	        Map<String, String> response = new HashMap<>();
//	        response.put("message", "로그인 실패: " + e.getMessage());
//	        
//	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)  // 401 Unauthorized
//	                             .body(response);
//	    }
//	}
	 
	/* 회원가입 기능 */
//	@PostMapping("/register")
//	public String signup(@ModelAttribute UserRequest userRequest, RedirectAttributes redirectAttributes) {
//		try {
//		
//			// UserRequest를 UserDto로 변환
//			UserDto userDto= userRequest.toDto();
//			
//			userService.registerUser(userDto);
//			log.info("회원가입에서 입력한 값 : " + userRequest); // 로그 추가
//			log.info("디비에저장되는값 : " + userDto); // 로그 추가
//			
//			//회원가입 성공 메시지 전달
//			redirectAttributes.addFlashAttribute("successMessage", "회원가입이 완료되었습니다. 로그인해주세요.");
//			return "redirect:/user/login";
//			
//		}catch(IllegalArgumentException e) {
//			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());  // 중복 아이디 메시지
//	        return "/user/register";  // 회원가입 페이지로 돌아감
//		}catch(Exception e) {
//			 System.out.println("회원가입 오류: " + 	e.getMessage()); 
//			 redirectAttributes.addFlashAttribute("errorMessage", "회원가입에 실패했습니다: " + e.getMessage());
//		        return "/user/register"; // 오류 발생 시 회원가입 페이지로 복귀
//		}
//	}
	
	
	
	
	
}
