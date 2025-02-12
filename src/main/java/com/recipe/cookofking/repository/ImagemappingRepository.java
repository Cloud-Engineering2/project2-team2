package com.recipe.cookofking.repository;

import com.recipe.cookofking.entity.Imagemapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImagemappingRepository extends JpaRepository<Imagemapping, Integer> {
    Optional<Imagemapping> findByIdAndS3Url(Integer id, String s3Url);

    Optional<Imagemapping> findByS3Url(String s3Url);

    @Query("SELECT a FROM Imagemapping a WHERE a.createdDate < :thresholdDate and a.isTemp=true")
    List<Imagemapping> findStaleImages(@Param("thresholdDate") LocalDateTime thresholdDate);

    List<Imagemapping> findByPost_Id(Integer postId);  // Post 객체 대신 postId로 검색

}