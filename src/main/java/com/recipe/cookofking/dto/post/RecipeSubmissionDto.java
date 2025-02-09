package com.recipe.cookofking.dto.post;

import com.recipe.cookofking.dto.image.ImageValidationDto;
import lombok.Data;

@Data
public class RecipeSubmissionDto {
    private ImageValidationDto validationData;
    private PostDto recipeData;
}
