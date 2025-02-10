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
    Integer id;
    String username;
    String email;
    String password;
    String role;
    LocalDateTime createdDate;
    LocalDateTime modifiedDate;

}