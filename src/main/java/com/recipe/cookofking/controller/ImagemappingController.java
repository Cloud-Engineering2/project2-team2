package com.recipe.cookofking.controller;

import com.recipe.cookofking.dto.image.ImagemappingDto;
import com.recipe.cookofking.service.ImagemappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ImagemappingController {

    private final ImagemappingService imagemappingService;

    @PostMapping
    @RequestMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            ImagemappingDto uploadedImage = imagemappingService.uploadImage(file);
            return ResponseEntity.ok(uploadedImage);
        } catch (Exception e) {
            // 에러 메시지와 스택 트레이스를 함께 반환
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}