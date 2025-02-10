package com.recipe.cookofking.service;

<<<<<<< HEAD

=======
>>>>>>> b767de0 (Second Version for Like)
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

<<<<<<< HEAD
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
=======
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
>>>>>>> b767de0 (Second Version for Like)
    }


    @Transactional(readOnly = true)
    public int getLikeCount(Integer postId) {
        // 📌 좋아요 수는 `Post` 엔티티에서 가져오기
        return postRepository.findById(postId)
                .map(Post::getLikeCount)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));
    }
}