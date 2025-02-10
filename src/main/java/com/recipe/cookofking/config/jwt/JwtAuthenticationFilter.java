package com.recipe.cookofking.config.jwt;


import java.io.IOException;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipe.cookofking.config.auth.PrincipalDetails;
import com.recipe.cookofking.dto.UserDto;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// 로그인처리 담당하는 필터
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	
	 private final AuthenticationManager authenticationManager;

	 public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
	        this.authenticationManager = authenticationManager;
	    }
	 
	 
	 
	 @Override
	 public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
	         throws AuthenticationException {
	     try {
	         log.info("로그인 요청 들어");

	         // 요청 본문을 UserDto로 바로 변환
	         UserDto userDto = new ObjectMapper().readValue(request.getInputStream(), UserDto.class);

	         if (userDto.getUsername() == null || userDto.getPassword() == null) {
	             throw new IllegalArgumentException("uid와 password가 필요합니다.");
	         }

	         // 인증 토큰 생성
	         UsernamePasswordAuthenticationToken authenticationToken = 
	             new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());

	         // 인증을 처리하고 인증 성공 후에 반환된 Authentication 객체
	         Authentication authentication = authenticationManager.authenticate(authenticationToken);

	         return authentication;
	     } catch (IOException e) {
	         log.error("요청 본문 파싱 오류", e);
	         throw new IllegalArgumentException("요청 데이터를 처리할 수 없습니다.");
	     }
	 }

	 @Override
	 protected void successfulAuthentication(HttpServletRequest request,
	                                          HttpServletResponse response,
	                                          FilterChain chain,
	                                          Authentication authResult) throws IOException, ServletException {
	     log.info("successfulAuthentication() 진입");

	     PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
	     log.info("PrincipalDetails: {}", principalDetails);

	     // JWT 토큰 생성
	     String jwtToken = JWT.create()
	             .withSubject(principalDetails.getUsername())
	             .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
	             .withClaim("uid", principalDetails.getUser().getUsername())
	             .sign(Algorithm.HMAC512(JwtProperties.SECRET));

	     // 토큰을 응답 본문으로 JSON 형태로 반환
	     response.setContentType("application/json");
	     response.getWriter().write("{\"token\":\"" + jwtToken + "\", \"redirect\":\"/\"}");  // 리다이렉트 정보와 토큰 함께 반환
	 }


	 
}
