package com.recipe.cookofking.dto;

import com.recipe.cookofking.entity.Imagemapping;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link Imagemapping}
 */
@Builder
@Value
public class ImagemappingDto implements Serializable {
    Integer id;
    PostDto post;
    String s3Url;
    Instant createdDate;
}