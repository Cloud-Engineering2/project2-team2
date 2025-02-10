package com.recipe.cookofking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")
@EntityListeners(AuditingEntityListener.class)  // 자동으로 생성일자 저장
public class Post {
    @Id
    @Column(name = "post_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Lob
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Lob
    @Column(name = "ingredients", columnDefinition = "TEXT")
    private String ingredients;

    @Lob
    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @Lob
    @Column(name = "mainImageS3URL")
    private String mainImageS3URL;

    @Builder.Default
    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;

    @Builder.Default
    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0;

    @Column(name = "createdDate")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "modifiedDate")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    // 조회수 감소 메서드
    public void decrementViewCount() {
        this.viewCount -= 1;
    }

    // 조회수 증가 메서드
    public void incrementViewCount() {
        this.viewCount += 1;
    }

    // 좋아요 수 감소 메서드 (필요 시)
    public void decrementLikeCount() {
        this.likeCount -= 1;
    }

    // 좋아요 수 증가 메서드 (필요 시)
    public void incrementLikeCount() {
        this.likeCount += 1;
    }
    public void updatePost(String title, String content, String ingredients, String instructions, String mainImageS3URL) {
        this.title = title;
        this.content = content;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.mainImageS3URL = mainImageS3URL;
    }
}