package com.recipe.cookofking.dto.request;

import java.time.LocalDateTime;

import com.recipe.cookofking.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserRequest {

    private String username;
    private String email;
    private String password;
    private String role;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    
    // UserDto로 변환하는 메소드
    public UserDto toDto() {
    	if (role == null) {
            role = "ROLE_USER";  // 기본값 설정
        }
        // 생성자에서 createdDate와 modifiedDate를 null로 설정
        return new UserDto(username, email, password, role);
    }
}

