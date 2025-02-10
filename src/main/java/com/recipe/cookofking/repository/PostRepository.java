package com.recipe.cookofking.repository;

import com.recipe.cookofking.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    // Add custom query methods if needed
}