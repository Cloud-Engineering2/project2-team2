package com.recipe.cookofking.dto;

import com.recipe.cookofking.entity.Like;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link Like}
 */
@Builder
@Value
public class LikeDto implements Serializable {
    Integer id;
    PostDto post;
    UserDto user;
    Instant createdDate;
}