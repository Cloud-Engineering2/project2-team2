package com.recipe.cookofking.controller;

import com.recipe.cookofking.dto.post.RecipeSubmissionDto;
import com.recipe.cookofking.service.ImagemappingService;
import com.recipe.cookofking.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
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

    // imageid가 null이고 url이 존재한다면 기존에 등록된 이미지이므로 그대로 사용
    // -> validation 필요없음
    // image id가 있다면 save 로직을 따라 검증 후 is_temp 플래그 해제
    // @TODO 기존에 사용되다가 삭제된 이미지는 어떻게 지울것인가?
    // temp 플래그를 되돌린 후 batch job으로 삭제?
    @PutMapping("/v1/update-recipe/{postId}")
    public ResponseEntity<String> updateRecipe(@PathVariable Integer postId, @RequestBody RecipeSubmissionDto submissionDto) {
        // 1. 이미지 검증
        imagemappingService.validateForUpdate(submissionDto.getValidationData());

        // 2. 레시피 저장
        postService.updatePost(submissionDto.getRecipeData(), postId);

        return ResponseEntity.ok("레시피가 성공적으로 수정되었습니다!");
    }
}
