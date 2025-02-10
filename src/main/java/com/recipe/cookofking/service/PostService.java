package com.recipe.cookofking.service;
import com.recipe.cookofking.dto.post.PostDto;
import com.recipe.cookofking.entity.Post;
import com.recipe.cookofking.mapper.PostMapper;
import com.recipe.cookofking.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

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
    public void updatePost(PostDto postDto) {
        Post post = postRepository.findById(postDto.getId()).orElse(null);
        if (post != null) {
            post = PostMapper.toEntity(postDto);
        }
    }
}
