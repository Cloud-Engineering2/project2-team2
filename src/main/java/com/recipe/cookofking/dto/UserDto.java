package com.recipe.cookofking.dto;

import com.recipe.cookofking.entity.User;


import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link User}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto implements Serializable {
   
	private static final long serialVersionUID = 1L;
	Integer id;
	String username;
    String email;
    String password;
    String role;
    LocalDateTime createdDate;
    LocalDateTime modifiedDate;
    
    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public UserDto(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdDate = null;  // 기본값 null
        this.modifiedDate = null; // 기본값 null
    }
    
    
    // 생성자 추가
    public static UserDto of( String username,  String password, String email, String role,LocalDateTime createdDate,LocalDateTime modifiedDate) {
    	return UserDto.of(username, password, email, role, null, null);
    }
    
    
 // Entity -> DTO 변환 메소드
    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .createdDate(user.getCreatedDate())
                .modifiedDate(user.getModifiedDate())
                .build();
    }
    
 // UserDto를  엔티티로 변환하는 메소드
    public User toEntity() {
        return new User( username, email, password, role, null, null);
    }

}