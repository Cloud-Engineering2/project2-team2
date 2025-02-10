package com.recipe.cookofking.config;


//@EnableMethodSecurity(prePostEnabled = true,securedEnabled = true)
//@EnableWebSecurity
//@Configuration

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // 모든 요청 허용
                )
                .csrf(csrf -> csrf.disable())  // CSRF 비활성화 (개발 환경)
                .formLogin(form -> form.disable());  // 폼 로그인 비활성화

        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder를 사용해 비밀번호 암호화
        return new BCryptPasswordEncoder();
    }
    
}
