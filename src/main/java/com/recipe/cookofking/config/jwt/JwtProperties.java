package com.recipe.cookofking.config.jwt;

public interface JwtProperties {

	String SECRET = "jwtSecret";
	int EXPIRATION_TIME = 60*60*24*1000;
	String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
