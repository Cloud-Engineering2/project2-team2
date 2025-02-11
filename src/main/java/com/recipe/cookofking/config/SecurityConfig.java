package com.recipe.cookofking.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.recipe.cookofking.config.jwt.JwtAuthenticationFilter;
import com.recipe.cookofking.config.jwt.JwtAuthorizationFilter;
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
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager ) throws Exception {
	
		http
    	// 로그인 시 JWT 토큰 발급을 위한 필터 추가 (JwtAuthenticationFilter)
    	.addFilter(new JwtAuthenticationFilter(authenticationManager))
    	
    	// 요청 시 JWT 토큰을 검증하는 필터 추가 (JwtAuthorizationFilter)
    	.addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository))
    	
    	.csrf(AbstractHttpConfigurer::disable)
    	
    	.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    	
    	.formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)

		// 경로별 권한 설정
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
				).permitAll()  // 로그인, 회원가입, 정적 리소스, 게시글 목록/보기는 인증 없이 접근 가능

				.requestMatchers("/post/edit/**", "/api/**").authenticated()  // 게시글 수정과 API 경로는 인증 필요

				.anyRequest().permitAll()  // 그 외 나머지 요청은 인증 없이 허용
		);

      
		return http.build();
	}
	
	
    
    
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder를 사용해 비밀번호 암호화
        return new BCryptPasswordEncoder();
    }
    
}
