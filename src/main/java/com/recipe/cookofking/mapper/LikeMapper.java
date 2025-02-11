package com.recipe.cookofking.mapper;

import com.recipe.cookofking.dto.LikeDto;
import com.recipe.cookofking.entity.Like;

public class LikeMapper {


    public static LikeDto toDto(Like like) {
        if (like == null) {
            return null;
        }
        return LikeDto.builder()
                .id(like.getId())
                .post(PostMapper.toDto(like.getPost()))
                //.user(UserMapper.toDto(like.getUser()))
                .createdDate(like.getCreatedDate())
                .build();
    }

    public static Like toEntity(LikeDto likeDto) {
        if (likeDto == null) {
            return null;
        }
        return Like.builder()
                .id(likeDto.getId())
                .post(PostMapper.toEntity(likeDto.getPost()))
                //.user(UserMapper.toEntity(likeDto.getUser()))
                .createdDate(likeDto.getCreatedDate())
                .build();
    }


}
