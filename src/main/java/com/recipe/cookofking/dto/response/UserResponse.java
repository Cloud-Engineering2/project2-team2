package com.recipe.cookofking.dto.response;

import com.recipe.cookofking.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    private String username;
    private String email;
    private String createdDate;

    public UserResponse(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.createdDate = user.getCreatedDate().toString();
    }
}

