package com.recipe.cookofking.service;
import com.recipe.cookofking.dto.post.PostDto;
import com.recipe.cookofking.dto.post.PostViewDto;
import com.recipe.cookofking.entity.Post;
import com.recipe.cookofking.mapper.PostMapper;
import com.recipe.cookofking.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;    @Transactional
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



    @Transactional
    public PostViewDto getPostById(Integer id) {
        // Post 엔티티 조회
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        // 조회수 증가
        post.incrementViewCount();
        postRepository.save(post);

        // PostViewDto로 변환하여 반환
        return PostViewDto.builder()
                .id(post.getId())
                .userid(post.getUser() != null ? post.getUser().getId() : null)
                .username(post.getUser() != null ? post.getUser().getUsername() : null)
                .title(post.getTitle())
                .content(post.getContent())
                .ingredients(post.getIngredients())
                .instructions(post.getInstructions())
                .mainImageS3URL(post.getMainImageS3URL())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .build();
    }

    public Page<PostDto> getPostList(Pageable pageable) {
        return postRepository.findAll(pageable).map(PostMapper::toDto);
    }

}
