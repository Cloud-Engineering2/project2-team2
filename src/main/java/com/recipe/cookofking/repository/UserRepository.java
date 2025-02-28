package com.recipe.cookofking.repository;

import java.util.Optional;

import com.recipe.cookofking.entity.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recipe.cookofking.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	Optional<User> findByUsername(String username);
	boolean existsByUsername(String username);  // username 중복 확인

}
