package com.recipe.cookofking.controller;


import com.recipe.cookofking.config.auth.PrincipalDetails;
import com.recipe.cookofking.service.LikeService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/likes")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }
    
    // 📌 좋아요 추가/취소 API
    @PostMapping
    public ResponseEntity<?> toggleLike(@RequestBody Map<String, Object> request,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        try {
            if (!request.containsKey("postId")) {
                return ResponseEntity.badRequest().body(Map.of("error", "postId가 필요합니다."));
            }

            Integer postId = (Integer) request.get("postId");
            Integer userId = principalDetails.getUser().getId();  // Get the user ID from PrincipalDetails

            Map<String, Object> result = likeService.toggleLike(postId, userId);  // Pass userId to the service

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // 📌 좋아요 개수 조회 API
//    @GetMapping("/{postId}")
//    public ResponseEntity<?> getLikeCount(@PathVariable(name = "postId") Integer postId) {
//        try {
//            Integer parsedPostId;
//
//            try {
//                parsedPostId = postId;
//            } catch (NumberFormatException e) {
//                return ResponseEntity.badRequest().body(Map.of("error", "postId 형식이 올바르지 않습니다."));
//            }
//
//            int likeCount = likeService.getLikeCount(parsedPostId);
//            return ResponseEntity.ok(Map.of("likeCount", likeCount));
//
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
//        }
//    }
}
