package com.recipe.cookofking.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostViewDto implements Serializable {
    private Integer id;
    private Integer userid;
    private String username;  // 작성자 이름
    private String title;
    private String content;
    private String ingredients;
    private String instructions;
    private String mainImageS3URL;
    private Integer viewCount;
    private Integer likeCount;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}