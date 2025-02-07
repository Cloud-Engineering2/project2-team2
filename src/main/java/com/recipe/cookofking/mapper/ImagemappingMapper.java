package com.recipe.cookofking.mapper;

import com.recipe.cookofking.dto.ImagemappingDto;
import com.recipe.cookofking.entity.Imagemapping;


public class ImagemappingMapper {

    public static ImagemappingDto toDto(Imagemapping imagemapping) {
        if (imagemapping == null) {
            return null;
        }
        return ImagemappingDto.builder()
                .id(imagemapping.getId())
                .post(PostMapper.toDto(imagemapping.getPost()))
                .s3Url(imagemapping.getS3Url())
                .createdDate(imagemapping.getCreatedDate())
                .build();
    }

    public static Imagemapping toEntity(ImagemappingDto imagemappingDto) {
        if (imagemappingDto == null) {
            return null;
        }
        return Imagemapping.builder()
                .id(imagemappingDto.getId())
                .post(PostMapper.toEntity(imagemappingDto.getPost()))
                .s3Url(imagemappingDto.getS3Url())
                .createdDate(imagemappingDto.getCreatedDate())
                .build();
    }
}

