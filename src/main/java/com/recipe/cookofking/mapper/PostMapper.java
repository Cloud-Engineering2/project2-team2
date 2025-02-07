package com.recipe.cookofking.mapper;

import com.recipe.cookofking.dto.PostDto;
import com.recipe.cookofking.entity.Post;

public class PostMapper {
    public static PostDto toDto(Post post) {
        if (post == null) {
            return null;
        }
        return PostDto.builder()
                .id(post.getId())
                .user(UserMapper.toDto(post.getUser()))
                .title(post.getTitle())
                .content(post.getContent())
                .ingredients(post.getIngredients())
                .instructions(post.getInstructions())
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .build();
    }

    public static Post toEntity(PostDto postDto) {
        if (postDto == null) {
            return null;
        }
        return Post.builder()
                .id(postDto.getId())
                .user(UserMapper.toEntity(postDto.getUser()))
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .ingredients(postDto.getIngredients())
                .instructions(postDto.getInstructions())
                .createdDate(postDto.getCreatedDate())
                .modifiedDate(postDto.getModifiedDate())
                .build();
    }


}
