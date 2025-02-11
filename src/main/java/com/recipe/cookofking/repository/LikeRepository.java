package com.recipe.cookofking.repository;

import com.recipe.cookofking.entity.Like;
import com.recipe.cookofking.entity.Post;
import com.recipe.cookofking.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
	Optional<Like> findByPost(Post post);  // 특정 게시글에 대한 좋아요 여부 확인
    int countByPost(Post post); // ✅ 좋아요 개수 조회
}
