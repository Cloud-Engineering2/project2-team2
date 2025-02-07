package com.recipe.cookofking.dto;

import com.recipe.cookofking.entity.User;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link User}
 */
@Builder
@Value
public class UserDto implements Serializable {
    Integer id;
    String username;
    String email;
    String password;
    String role;
    Instant createdDate;
    Instant modifiedDate;

}