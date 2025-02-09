package com.recipe.cookofking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "imagemapping")
@EntityListeners(AuditingEntityListener.class)  // 자동으로 생성일자 저장
public class Imagemapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    @Column(name = "s3_url", nullable = false)
    private String s3Url;

    @Column(name = "is_temp", nullable = false)
    private boolean isTemp;  // 이미지 임시 상태 플래그 (작성 중 true, 작성 완료 false)

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    // isTemp 상태를 변경하는 명확한 메서드
    public void markAsPermanent() {
        this.isTemp = false;
    }
}
