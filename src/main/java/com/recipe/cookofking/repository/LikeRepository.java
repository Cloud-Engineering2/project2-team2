package com.recipe.cookofking.repository;

import com.recipe.cookofking.entity.Like;
import com.recipe.cookofking.entity.Post;
import com.recipe.cookofking.entity.User;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByPostIdAndUserId(Integer postId, Integer userId);
    Optional<Like> findByPostAndUser(Post post, User user); // 특정 게시글에 대한 좋아요 여부 확인

    @Query("SELECT l.post FROM Like l WHERE l.user.id = :userId")
    Page<Post> findPostsByUserId(@Param("userId") Integer userId, Pageable pageable);

    int countByPost(Post post); // ✅ 좋아요 개수 조회
}
