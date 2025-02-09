package com.recipe.cookofking.dto.image;

import lombok.Data;

import java.util.List;

@Data
public class ImageValidationDto {
    private Integer mainImageId;
    private String mainImageUrl;
    private List<StepImageDto> stepImages;

    @Data
    public static class StepImageDto {
        private Integer imageId;
        private String imageUrl;
    }
}