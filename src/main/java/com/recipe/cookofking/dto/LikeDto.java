package com.recipe.cookofking.dto;

import com.recipe.cookofking.dto.post.PostDto;
import com.recipe.cookofking.entity.Like;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Like}
 */
@Builder
@Value
public class LikeDto implements Serializable {
    Integer id;
    PostDto post;
    UserDto user;
    LocalDateTime createdDate;
}