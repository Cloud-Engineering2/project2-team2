package com.recipe.cookofking.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.recipe.cookofking.config.auth.PrincipalDetails;
import com.recipe.cookofking.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final UserRepository userRepository;
	
	@Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
  }
	
	
	
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()  // 모든 요청 허용
//                )
//                .csrf(csrf -> csrf.disable())  // CSRF 비활성화 (개발 환경)
//                .formLogin(form -> form.disable());  // 폼 로그인 비활성화
//
//        return http.build();
//    }
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (필요시 활성화)
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers(
	                "/user/login",
	                "/user/register",
	                "/js/**",
	                "/css/**",
	                "/images/**",
	                "/static/**",
	                "/post/list",
	                "/post/view/**"
	            ).permitAll()  // 인증 없이 접근 가능
	            .requestMatchers("/post/edit/**", "/api/**").authenticated()  // 인증 필요
	            .anyRequest().permitAll()
	        )
	        .sessionManagement(session -> session
	            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 필요시 세션 생성
	        )
	        .formLogin(form -> form
	            .loginPage("/user/login")  // 로그인 페이지 지정
	            .loginProcessingUrl("/user/login") // 로그인 요청 처리 URL
	            .defaultSuccessUrl("/post/write", true) // 로그인 성공 시 이동할 페이지
	            .permitAll()
	        )
	        .logout(logout -> logout
	            .logoutUrl("/user/logout") // 로그아웃 요청 URL
	            .logoutSuccessUrl("/user/login") // 로그아웃 성공 시 이동할 페이지
	            .invalidateHttpSession(true) // 세션 무효화
	            .deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제
	            .permitAll()
	        );

	    return http.build();
	}
    
	// 로그인 성공 후 처리하는 서비스나 필터에서
	public void loginSuccessHandler(PrincipalDetails principalDetails) {
	    Authentication authentication = new UsernamePasswordAuthenticationToken(
	            principalDetails,  // PrincipalDetails 객체 (로그인한 사용자의 정보)
	            null,  // 비밀번호는 null로 설정 (이미 검증되었으므로)
	            principalDetails.getAuthorities()  // 권한 정보
	    );

	    // SecurityContext에 Authentication 객체를 설정
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	}
    
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder를 사용해 비밀번호 암호화
        return new BCryptPasswordEncoder();
    }
    
}
