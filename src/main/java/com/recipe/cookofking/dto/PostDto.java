package com.recipe.cookofking.dto;

import com.recipe.cookofking.entity.Post;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link Post}
 */
@Builder
@Value
public class PostDto implements Serializable {
    Integer id;
    UserDto user;
    String title;
    String content;
    String ingredients;
    String instructions;
    Instant createdDate;
    Instant modifiedDate;
}