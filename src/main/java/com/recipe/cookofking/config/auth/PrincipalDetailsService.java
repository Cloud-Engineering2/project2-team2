package com.recipe.cookofking.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.recipe.cookofking.entity.User;
import com.recipe.cookofking.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

	 private final UserRepository userRepository;
	
	 @Override
	 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	   
		 // Optional<User>에서 User를 안전하게 꺼내기
	        User user = userRepository.findByUsername(username)
	                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

		 return new PrincipalDetails(user);
	    
	  
	 }


}
