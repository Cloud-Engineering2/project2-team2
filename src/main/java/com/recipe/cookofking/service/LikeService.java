package com.recipe.cookofking.service;

import com.recipe.cookofking.dto.LikeDto;
import com.recipe.cookofking.repository.LikeRepository;
import com.recipe.cookofking.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    // Add Like related business logic here
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    public LikeService(LikeRepository likeRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
    }

    public LikeDto getLikes(Integer postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post not found");
        }

        long likesCount = likeRepository.countByPostId(postId);
        return new LikeDto();
    }
}
