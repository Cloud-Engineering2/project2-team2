package com.recipe.cookofking.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.recipe.cookofking.config.jwt.JwtProperties;
import com.recipe.cookofking.dto.UserDto;
import com.recipe.cookofking.dto.post.RecipeSubmissionDto;
import com.recipe.cookofking.service.ImagemappingService;
import com.recipe.cookofking.service.PostService;
import com.recipe.cookofking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public ResponseEntity<Map<String, Object>> submitRecipe(@RequestBody RecipeSubmissionDto submissionDto,
                                               @RequestHeader("Authorization") String authorizationHeader) {
        // JWT 토큰에서 username 추출
        String token = authorizationHeader.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                .build()
                .verify(token);

        String username = decodedJWT.getClaim("uid").asString();  // 'uid' 클레임에서 username 추출

        System.out.println(username);

        // UserService에서 UserDto 조회
        UserDto userDto = userService.findUserByUsername(username);

        // PostDto에 UserDto 설정
        submissionDto.getRecipeData().setUser(userDto);

        System.out.println(userDto);

        // 이미지 검증
        imagemappingService.validateAndMarkPermanent(submissionDto.getValidationData());

        Integer postId = postService.savePost(submissionDto.getRecipeData());

        // postId를 포함한 응답 반환
        Map<String, Object> response = new HashMap<>();
        response.put("message", "레시피가 성공적으로 저장되었습니다!");
        response.put("postId", postId);

        return ResponseEntity.ok(response);
    }
}
