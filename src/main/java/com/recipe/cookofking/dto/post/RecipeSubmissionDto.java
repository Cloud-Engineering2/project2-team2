package com.recipe.cookofking.dto.post;

import com.recipe.cookofking.dto.image.ImageValidationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeSubmissionDto {
    private ImageValidationDto validationData;
    private PostDto recipeData;
}
