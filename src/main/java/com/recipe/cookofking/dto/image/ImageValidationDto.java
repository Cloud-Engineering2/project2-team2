package com.recipe.cookofking.dto.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageValidationDto {
    private ImageInfoDto mainImage = new ImageInfoDto();  // 기본값 추가
    private List<ImageInfoDto> stepImages = new ArrayList<>();
    private List<String> orphanedUrls = new ArrayList<>();

    @Data
    public static class ImageInfoDto {
        private Integer imageId;
        private String imageUrl;
    }
}