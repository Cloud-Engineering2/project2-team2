package com.recipe.cookofking.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.recipe.cookofking.dto.UserDto;
import com.recipe.cookofking.dto.image.ImageValidationDto;
import com.recipe.cookofking.dto.post.RecipeSubmissionDto;
import com.recipe.cookofking.service.ImagemappingService;
import com.recipe.cookofking.service.PostService;
import com.recipe.cookofking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;
    private final ImagemappingService imagemappingService;
    private final UserService userService;

    // 레시피 저장 API
    @PostMapping("/submit-recipe")
    public ResponseEntity<Map<String, Object>> submitRecipe(@RequestBody RecipeSubmissionDto submissionDto) {
        // JWT 토큰에서 username 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // 인증된 사용자 이름

        // UserService에서 UserDto 조회
        UserDto userDto = userService.findUserByUsername(username);
        submissionDto.getRecipeData().setUser(userDto);

        // 이미지 검증 및 영구 저장 처리
        imagemappingService.validateAndMarkPermanent(submissionDto.getValidationData());

        // 레시피 저장
        Integer postId = postService.savePost(submissionDto.getRecipeData());

        // 메인 이미지 및 요리 순서 이미지의 postId 갱신
        List<String> allImageUrls = collectAllImageUrls(submissionDto.getValidationData());
        imagemappingService.updateImagePostId(allImageUrls, postId);

        // 응답 반환
        Map<String, Object> response = new HashMap<>();
        response.put("message", "레시피가 성공적으로 저장되었습니다!");
        response.put("postId", postId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/update-recipe")
    public ResponseEntity<Map<String, Object>> updateRecipe(@RequestBody RecipeSubmissionDto submissionDto) {
        // JWT 토큰에서 사용자 정보 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // 인증된 사용자 이름

//        String token = authorizationHeader.replace("Bearer ", "");
//        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token);
//        String username = decodedJWT.getClaim("uid").asString();

        // 사용자 정보 조회
        UserDto userDto = userService.findUserByUsername(username);
        submissionDto.getRecipeData().setUser(userDto);

        // 게시글 ID 확인
        Integer postId = submissionDto.getRecipeData().getId();
        if (postId == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "게시글 ID가 누락되었습니다. 수정 요청에는 게시글 ID가 필요합니다.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // 게시글 소유자 검증
        if (!postService.isUserOwnerOfPost(postId, userDto.getId())) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "해당 게시글에 대한 수정 권한이 없습니다.");
            return ResponseEntity.status(403).body(errorResponse);  // 403 Forbidden
        }

        // 고아 이미지 소유자 검증
        if (!validateOrphanedImageOwnership(submissionDto.getValidationData(), postId)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "다른 게시글에 속한 이미지를 삭제할 수 없습니다.");
            return ResponseEntity.status(403).body(errorResponse);  // 403 Forbidden
        }

        // 이미지 검증 및 영구 저장 처리
        imagemappingService.validateAndMarkPermanent(submissionDto.getValidationData());

        // 게시글 수정
        Integer updatedPostId = postService.updatePost(submissionDto.getRecipeData());

        // 새로 추가된 이미지들의 postId 갱신
        List<String> allImageUrls = collectAllImageUrls(submissionDto.getValidationData());
        imagemappingService.updateImagePostId(allImageUrls, updatedPostId);

        // 성공 응답 반환
        Map<String, Object> response = new HashMap<>();
        response.put("message", "레시피가 성공적으로 수정되었습니다!");
        response.put("postId", updatedPostId);

        return ResponseEntity.ok(response);
    }


    private List<String> collectAllImageUrls(ImageValidationDto validationData) {
        List<String> imageUrls = new ArrayList<>();

        // 메인 이미지 URL 추가
        if (validationData.getMainImage() != null && validationData.getMainImage().getImageUrl() != null) {
            imageUrls.add(validationData.getMainImage().getImageUrl());
        }

        // 요리 순서 이미지 URL 추가
        validationData.getStepImages().forEach(stepImage -> {
            if (stepImage.getImageUrl() != null) {
                imageUrls.add(stepImage.getImageUrl());
            }
        });

        return imageUrls;
    }

    private boolean validateOrphanedImageOwnership(ImageValidationDto validationData, Integer postId) {
        for (String orphanedUrl : validationData.getOrphanedUrls()) {
            boolean isOrphanedImageLinked = imagemappingService.isImageLinkedToPost(orphanedUrl, postId);
            if (!isOrphanedImageLinked) {
                return false;  // 고아 이미지가 현재 게시글에 속하지 않음
            }
        }
        return true;  // 모든 고아 이미지가 현재 게시글에 속함
    }


}
