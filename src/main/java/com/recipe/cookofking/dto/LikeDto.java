package com.recipe.cookofking.dto;

import com.recipe.cookofking.dto.post.PostDto;
import com.recipe.cookofking.entity.Like;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Like}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeDto implements Serializable {
    Integer id;
    PostDto post;
    UserDto user;
    LocalDateTime createdDate;
}