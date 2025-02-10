package com.recipe.cookofking.repository;
import com.recipe.cookofking.entity.Imagemapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImagemappingRepository extends JpaRepository<Imagemapping, Integer> {
    Optional<Imagemapping> findByIdAndS3Url(Integer id, String s3Url);
    Optional<Imagemapping> findByS3Url(String s3Url);
}