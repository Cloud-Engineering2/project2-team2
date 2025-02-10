package com.recipe.cookofking.dto.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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