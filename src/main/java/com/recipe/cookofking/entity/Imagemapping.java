package com.recipe.cookofking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "imagemapping")
public class Imagemapping {
    @Id
    @Column(name = "image_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "s3_url", nullable = false)
    private String s3Url;

    @Column(name = "createdDate")
    private Instant createdDate;

}