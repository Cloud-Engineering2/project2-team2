package com.recipe.cookofking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@Entity
@Table(name = "user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)  // 자동으로 생성일자 저장
public class User {
    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "email", nullable = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @ColumnDefault("'ROLE_USER'")
    @Column(name = "role", nullable = false, length = 20)
    private String role;

    @Column(name = "createdDate")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "modifiedDate")
    @LastModifiedDate
    private LocalDateTime modifiedDate;
    
    // User 엔티티 생성
    public User(String username, String email, String password, String role, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
    
 // ⭐ 업데이트 전용 메서드 추가
    public void updateUser(String username, String email) {
        this.username = username;
        this.email = email;
        this.modifiedDate = LocalDateTime.now(); // 수정 날짜 갱신
    }

}