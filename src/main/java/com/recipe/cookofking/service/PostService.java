package com.recipe.cookofking.service;
import com.recipe.cookofking.dto.post.PostDto;
import com.recipe.cookofking.dto.post.PostViewDto;
import com.recipe.cookofking.entity.Post;
import com.recipe.cookofking.entity.User;
import com.recipe.cookofking.mapper.PostMapper;
import com.recipe.cookofking.repository.PostRepository;
import com.recipe.cookofking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Integer savePost(PostDto postDto) {
        // 1. DB에서 User 엔티티 조회
        User user = userRepository.findById(postDto.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2. Post 엔티티 생성 (User 엔티티 포함)
        Post post = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .ingredients(postDto.getIngredients())
                .instructions(postDto.getInstructions())
                .mainImageS3URL(postDto.getMainImageS3URL())
                .user(user)  // 영속 상태의 User 엔티티 설정
                .build();

        // 3. 레시피 저장
        Post savedPost = postRepository.save(post);

        // 4. 저장된 게시글의 ID 반환
        return savedPost.getId();
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
