package com.recipe.cookofking.controller;


import com.recipe.cookofking.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<?> toggleLike(@RequestBody Map<String, Object> request) {
        try {
            // 🔥 값이 `null`이거나 형 변환이 안 되는 경우 방지
            if (!request.containsKey("postId") || !request.containsKey("userId")) {
                return ResponseEntity.badRequest().body(Map.of("error", "postId와 userId가 필요합니다."));
            }

            int postId;
            int userId;

            try {
                postId = (int) request.get("postId");
                userId = (int) request.get("userId");
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body(Map.of("error", "postId 또는 userId 형식이 올바르지 않습니다."));
            }

            int likeCount = likeService.toggleLike(postId, userId);
            return ResponseEntity.ok(Map.of("likeCount", likeCount));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // 📌 좋아요 개수 조회 API
    @GetMapping("/{postId}")
    public ResponseEntity<?> getLikeCount(@PathVariable(name = "postId") Integer postId) {
        try {
            Integer parsedPostId;
            
            try {
                parsedPostId = postId;
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body(Map.of("error", "postId 형식이 올바르지 않습니다."));
            }

            int likeCount = likeService.getLikeCount(parsedPostId);
            return ResponseEntity.ok(Map.of("likeCount", likeCount));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
