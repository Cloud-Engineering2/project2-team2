package com.recipe.cookofking.dto.image;

import com.recipe.cookofking.dto.post.PostDto;
import com.recipe.cookofking.entity.Imagemapping;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Imagemapping}
 */
@Builder
@Value
public class ImagemappingDto implements Serializable {
    Integer id;
    PostDto post;
    String s3Url;
    boolean isTemp;
    LocalDateTime createdDate;
}