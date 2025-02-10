package com.recipe.cookofking.controller;

import com.recipe.cookofking.dto.post.RecipeSubmissionDto;
import com.recipe.cookofking.service.ImagemappingService;
import com.recipe.cookofking.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;
    private final ImagemappingService imagemappingService;
    // 레시피 저장 API
    @PostMapping("/submit-recipe")
    public ResponseEntity<String> submitRecipe(@RequestBody RecipeSubmissionDto submissionDto) {
        // 1. 이미지 검증
        imagemappingService.validateAndMarkPermanent(submissionDto.getValidationData());

        // 2. 레시피 저장
        postService.savePost(submissionDto.getRecipeData());

        return ResponseEntity.ok("레시피가 성공적으로 저장되었습니다!");
    }
}
