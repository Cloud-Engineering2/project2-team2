package com.recipe.cookofking.service;

import com.recipe.cookofking.entity.Like;
import com.recipe.cookofking.entity.Post;
import com.recipe.cookofking.entity.User;
import com.recipe.cookofking.repository.LikeRepository;
import com.recipe.cookofking.repository.PostRepository;
import com.recipe.cookofking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikeService(LikeRepository likeRepository, PostRepository postRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 좋아요 추가/취소
    @Transactional
    public int toggleLike(Integer postId, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get()); // 이미 좋아요 했으면 취소
        } else {
            Like newLike = Like.builder().user(user).post(post).build();
            likeRepository.save(newLike); // 좋아요 추가
        }

        return likeRepository.countByPost(post); // 📌 반환 타입 맞추기
    }

    // 좋아요 조회
    @Transactional(readOnly = true)
    public int getLikeCount(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        return likeRepository.countByPost(post); // 📌 Long 변환
    }
}
