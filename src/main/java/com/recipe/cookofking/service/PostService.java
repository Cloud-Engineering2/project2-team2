package com.recipe.cookofking.service;
import com.recipe.cookofking.dto.post.PostDto;
import com.recipe.cookofking.dto.post.PostViewDto;
import com.recipe.cookofking.entity.Post;
import com.recipe.cookofking.entity.User;
import com.recipe.cookofking.mapper.PostMapper;
import com.recipe.cookofking.repository.LikeRepository;
import com.recipe.cookofking.repository.PostRepository;
import com.recipe.cookofking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
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

    @Transactional(readOnly = true)
    public boolean isUserOwnerOfPost(Integer postId, Integer userId) {
        return postRepository.findById(postId)
                .map(post -> post.getUser().getId().equals(userId))
                .orElse(false);  // 게시글이 존재하지 않으면 false 반환
    }



    @Transactional
    public Integer updatePost(PostDto postDto) {
        // 기존 게시글 조회
        Post existingPost = postRepository.findById(postDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. ID: " + postDto.getId()));

        // 정적 팩토리 메서드로 새로운 엔티티 생성
        Post updatedPost = existingPost.updateFromDto(postDto);

        // 수정된 엔티티 저장
        postRepository.save(updatedPost);

        return updatedPost.getId();
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

    @Transactional(readOnly = true)
    public Page<PostDto> getMyPostList(Pageable pageable, Integer userId) {
        return postRepository.findByUserId(userId, pageable)
                .map(PostMapper::toDto);
    }

    @Transactional(readOnly = true)
    public boolean hasUserLikedPost(Integer postId, Integer userId) {
        return likeRepository.findByPostIdAndUserId(postId, userId).isPresent();
    }

    @Transactional(readOnly = true)
    public Page<PostDto> getMyScrapList(Pageable pageable, Integer userId) {
        return likeRepository.findPostsByUserId(userId, pageable)
                .map(PostMapper::toDto);
    }


    @Transactional
    public void deletePost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
        postRepository.delete(post);
    }

    public void validatePostOwner(Integer postId, String currentUsername) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        // 1. 로그인 여부 확인
        if (currentUsername == null || currentUsername.isEmpty()) {
            throw new AccessDeniedException("로그인 후 수정 가능합니다.");
        }

        // 2. 탈퇴한 회원 여부 확인 (post.getUser()가 null인 경우)
        if (post.getUser() == null) {
            throw new AccessDeniedException("탈퇴한 회원의 게시글은 수정할 수 없습니다.");
        }

        // 3. 게시글 소유자 확인
        if (!post.getUser().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("해당 게시글에 대한 수정 권한이 없습니다.");
        }
    }


}
