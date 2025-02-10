package com.recipe.cookofking.repository;

import com.recipe.cookofking.entity.Like;
import com.recipe.cookofking.entity.Post;
import com.recipe.cookofking.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    // Add custom query methods if needed

    Optional<Like> findByUserAndPost(User user, Post post); // 특정 유저가 특정 게시글을 좋아요 했는지 조회
    int countByPost(Post post); // 특정 게시글의 좋아요 개수 조회
}