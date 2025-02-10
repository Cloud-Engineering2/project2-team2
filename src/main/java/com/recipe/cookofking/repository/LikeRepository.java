package com.recipe.cookofking.repository;
import com.recipe.cookofking.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    // Add custom query methods if needed
	long countByPostId(Integer postId); // 특정 게시물의 좋아요 개수를 반환
}