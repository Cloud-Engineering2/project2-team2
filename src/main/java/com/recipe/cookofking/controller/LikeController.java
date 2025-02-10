package com.recipe.cookofking.controller;

import com.recipe.cookofking.service.LikeService;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
            if (!request.containsKey("postId")) {
                return ResponseEntity.badRequest().body(Map.of("error", "postId가 필요합니다."));
            }

            Integer postId = (Integer) request.get("postId");
            int likeCount = likeService.toggleLike(postId);

            return ResponseEntity.ok(Map.of("likeCount", likeCount));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // 📌 좋아요 개수 조회 API
    @GetMapping("/{postId}")
    public ResponseEntity<?> getLikeCount(@PathVariable(name = "postId") Integer postId) {
        int likeCount = likeService.getLikeCount(postId);
 //       boolean isLiked = likeService.isLikedByUser(postId);  // ✅ 유저가 좋아요 했는지 확인

        return ResponseEntity.ok(Map.of(
            "likeCount", likeCount
//          "isLiked", isLiked
        ));
    }
}