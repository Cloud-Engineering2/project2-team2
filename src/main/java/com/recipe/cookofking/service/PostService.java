package com.recipe.cookofking.service;

import com.recipe.cookofking.dto.post.PostDto;
import com.recipe.cookofking.entity.Post;
import com.recipe.cookofking.mapper.PostMapper;
import com.recipe.cookofking.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public void savePost(PostDto postDto) {
        // 1. Post 엔티티 생성
        Post post = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .ingredients(postDto.getIngredients())  // JSON 문자열로 저장
                .instructions(postDto.getInstructions())  // JSON 문자열로 저장
                .mainImageS3URL(postDto.getMainImageS3URL())
                .build();

        // 2. 레시피 저장
        Post savedPost = postRepository.save(post);
    }

    public PostDto getPostById(Integer id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NoSuchElementException("레시피를 찾을 수 없습니다"));
        return PostMapper.toDto(post);
    }

    @Transactional
    public void updatePost(PostDto postDto, Integer postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            post.updatePost(
                    postDto.getTitle(),
                    postDto.getContent(),
                    postDto.getIngredients(),
                    postDto.getInstructions(),
                    postDto.getMainImageS3URL()
            );
        }
    }
}
