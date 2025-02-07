package com.recipe.cookofking.repository;
import com.recipe.cookofking.entity.Imagemapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ImagemappingRepository extends JpaRepository<Imagemapping, Integer> {
    // Add custom query methods if needed
}