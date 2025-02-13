package com.recipe.cookofking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

	@Bean
	   public CorsFilter corsFilter() {
	      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	      CorsConfiguration config = new CorsConfiguration();
	      config.setAllowCredentials(true); // 응답 json 결과를 js 처리 가능 설정
	      config.addAllowedOrigin("*"); 	// cors 허가 도메인
	      config.addAllowedHeader("*");		// 허가 헤더 정보값
	      config.addAllowedMethod("*");		// 허가 메서드
	      config.addExposedHeader("Authorization"); // Authorization 헤더 노출
	      source.registerCorsConfiguration("/**", config);
	      return new CorsFilter(source);
	   }
}
