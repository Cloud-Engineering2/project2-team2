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
    
    @Transactional
    public int toggleLike(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        Optional<Like> existingLike = likeRepository.findByPost(post);

        if (existingLike.isPresent()) {
            // 🚨 기존 좋아요가 있다면 삭제
            likeRepository.delete(existingLike.get());
            post.decrementLikeCount(); // ✅ Post의 좋아요 수 감소
        } else {
            // 🚀 기존 좋아요가 없다면 추가
            Like newLike = Like.builder().post(post).build();
            likeRepository.save(newLike);
            post.incrementLikeCount(); // ✅ Post의 좋아요 수 증가
        }

        postRepository.save(post); // ✅ 좋아요 수 저장 (중요!)
        return post.getLikeCount(); // 🚀 변경된 좋아요 수 반환
    }
    
    @Transactional
    public int getLikeCount(Integer postId) {
    	Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. postId: " + postId));
    	
        // ✅ 게시글의 좋아요 수 반환
        return likeRepository.countByPost(post);
    }
}
