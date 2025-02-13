package com.recipe.cookofking.service;

import com.recipe.cookofking.entity.Like;
import com.recipe.cookofking.entity.Post;
import com.recipe.cookofking.entity.User;
import com.recipe.cookofking.repository.LikeRepository;
import com.recipe.cookofking.repository.PostRepository;
import com.recipe.cookofking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
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

    @Transactional
    public Map<String, Object> toggleLike(Integer postId, Integer userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Optional<Like> existingLike = likeRepository.findByPostAndUser(post, user);
        String action;
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            post.decrementLikeCount();
            action = "unscrapped";
        } else {
            Like newLike = Like.builder().post(post).user(user).build();
            likeRepository.save(newLike);
            post.incrementLikeCount();
            action = "scrapped";
        }

        postRepository.save(post);
        return Map.of(
                "likeCount", post.getLikeCount(),
                "action", action
        );
    }



//    @Transactional
//    public int getLikeCount(Integer postId) {
//    	Post post = postRepository.findById(postId).orElseThrow(() ->
//    	new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. postId: " + postId));
//
//        // ✅ 게시글의 좋아요 수 반환
//        return likeRepository.countByPost(post);
//    }
}
