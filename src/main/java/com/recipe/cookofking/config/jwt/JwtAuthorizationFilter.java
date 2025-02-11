package com.recipe.cookofking.config.jwt;


import java.io.IOException;


import jakarta.servlet.http.Cookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.recipe.cookofking.config.auth.PrincipalDetails;
import com.recipe.cookofking.entity.User;
import com.recipe.cookofking.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;
    

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }
    

    // 로그인요청ㅊ
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 1. Authorization 헤더에서 토큰 추출 (API 요청용)
        String header = request.getHeader("Authorization");
        String token = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.replace("Bearer ", "");
        } else {
            // 2. Authorization 헤더가 없으면 쿠키에서 토큰 추출 (페이지 요청용)
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("token".equals(cookie.getName())) {
                        token = cookie.getValue();
                    }
                }
            }
        }


        if (token != null) {
            try {
                String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                        .build()
                        .verify(token)
                        .getClaim("username")
                        .asString();

                if (username != null) {
                    User user = userRepository.findByUsername(username)
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    PrincipalDetails principalDetails = new PrincipalDetails(user);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            principalDetails, null, principalDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                log.error("JWT 처리 중 오류 발생", e);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        chain.doFilter(request, response);
//    	// 헤더에서 Authorization 키 확인
//        String header = request.getHeader("Authorization");
//
//        if (header == null || !header.startsWith("Bearer ")) {
//            chain.doFilter(request, response);
//            return;
//        }
//        // Authorization 헤더에서 토큰 추출
//        String token = header.replace("Bearer ", "");
//
//        try {
//            String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
//                    .build()
//                    .verify(token)
//                    .getClaim("uid")
//                    .asString();
//
//            if (username != null) {
//                User user = userRepository.findByUsername(username)
//                        .orElseThrow(() -> new RuntimeException("User not found"));
//                PrincipalDetails principalDetails = new PrincipalDetails(user);
//
//                Authentication authentication = new UsernamePasswordAuthenticationToken(
//                        principalDetails, null, principalDetails.getAuthorities());
//
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        } catch (Exception e) {
//            log.error("JWT 처리 중 오류 발생", e);
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            response.getWriter().write("Forbidden: Invalid or expired token");
//            return;
//        }
//
//        chain.doFilter(request, response);
    }


}
