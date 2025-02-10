package com.recipe.cookofking.repository;

import com.recipe.cookofking.entity.Like;
import com.recipe.cookofking.entity.Post;
<<<<<<< HEAD
=======
import com.recipe.cookofking.entity.User;
>>>>>>> b767de0 (Second Version for Like)

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
<<<<<<< HEAD
	Optional<Like> findByPost(Post post);  // 특정 게시글에 대한 좋아요 여부 확인
    int countByPost(Post post); // ✅ 좋아요 개수 조회
}
=======
    // Add custom query methods if needed
	
    Optional<Like> findByUserAndPost(User user, Post post); // 특정 유저가 특정 게시글을 좋아요 했는지 조회
    int countByPost(Post post); // 특정 게시글의 좋아요 개수 조회
}
>>>>>>> b767de0 (Second Version for Like)
