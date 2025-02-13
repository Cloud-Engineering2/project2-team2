package com.recipe.cookofking.service;

import com.recipe.cookofking.dto.image.ImageValidationDto;
import com.recipe.cookofking.dto.image.ImagemappingDto;
import com.recipe.cookofking.entity.Imagemapping;
import com.recipe.cookofking.entity.Post;
import com.recipe.cookofking.mapper.ImagemappingMapper;
import com.recipe.cookofking.repository.ImagemappingRepository;
import com.recipe.cookofking.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImagemappingService {

    private final S3Client s3Client;
    private final ImagemappingRepository imagemappingRepository;
    private final PostRepository postRepository;

    // application.properties에서 버킷 이름 주입
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.cloudfront.domain}")
    private String cloudFrontDomain;


    @Transactional
    public ImagemappingDto uploadImage(MultipartFile file) throws IOException {
        // S3에 업로드할 파일명 생성
        String fileName = "recipes/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        // S3 업로드 요청 생성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)  // 주입된 버킷 이름 사용
                .key(fileName)
                .build();

        // S3에 파일 업로드
        s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        // 업로드된 파일의 S3 URL 생성
        String region = s3Client.serviceClientConfiguration().region().id();
//        String s3Url = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
        String s3Url = cloudFrontDomain + fileName;

        // DB에 이미지 정보 저장
        Imagemapping imagemapping = Imagemapping.builder()
                .s3Url(s3Url)
                .isTemp(true)
                .build();

        Imagemapping savedImage = imagemappingRepository.save(imagemapping);

        // 저장된 이미지 정보를 DTO로 변환하여 반환
        return ImagemappingMapper.toDto(savedImage);
    }
    @Transactional
    public void validateAndMarkPermanent(ImageValidationDto validationDto) {

        // 1. 메인 이미지 검증 및 영구 저장
        if (validationDto.getMainImage() != null && validationDto.getMainImage().getImageId() != null) {
            System.out.println("Validating main image: " + validationDto.getMainImage().getImageUrl());
            imagemappingRepository.findByIdAndS3Url(validationDto.getMainImage().getImageId(), validationDto.getMainImage().getImageUrl())
                    .ifPresentOrElse(image -> {
                        image.markAsPermanent();
                        imagemappingRepository.save(image);
                        System.out.println("Main image marked as permanent.");
                    }, () -> {
                        System.out.println("Main image not found for marking.");
                    });
        }

        // 2. 조리 순서 이미지 검증 및 영구 저장
        validationDto.getStepImages().forEach(stepImage -> {
            if (stepImage.getImageId() != null) {
                System.out.println("Validating step image with ID: " + stepImage.getImageId() + ", URL: " + stepImage.getImageUrl());
                imagemappingRepository.findByIdAndS3Url(stepImage.getImageId(), stepImage.getImageUrl())
                        .ifPresentOrElse(image -> {
                            image.markAsPermanent();
                            imagemappingRepository.save(image);
                            System.out.println("Step image marked as permanent.");
                        }, () -> {
                            System.out.println("Step image not found for marking.");
                        });
            }
        });

        // 3. 고아 이미지 처리 (영구 저장 해제 또는 삭제)
        if (validationDto.getOrphanedUrls() != null && !validationDto.getOrphanedUrls().isEmpty()) {
            System.out.println("Processing orphaned images: " + validationDto.getOrphanedUrls());

            validationDto.getOrphanedUrls().forEach(orphanUrl -> {
                System.out.println("Checking orphaned image with URL: " + orphanUrl);
                imagemappingRepository.findByS3Url(orphanUrl)
                        .ifPresentOrElse(orphanedImage -> {
                            orphanedImage.unmarkAsPermanent();  // 영구 저장 해제
                            imagemappingRepository.save(orphanedImage);
                            System.out.println("Orphaned image unmarked as permanent.");
                        }, () -> {
                            System.out.println("Orphaned image not found in repository.");
                        });
            });
        } else {
            System.out.println("No orphaned images to process.");
        }

        System.out.println("Image validation and marking process completed.");
    }

    // 게시글 ID로 이미지 임시 처리
    @Transactional
    public void markImagesAsTemporaryByPostId(Integer postId) {
        List<Imagemapping> images = imagemappingRepository.findByPost_Id(postId);  // 수정된 부분
        for (Imagemapping image : images) {
            image.unmarkAsPermanent();
            image.setPost(null);
            imagemappingRepository.save(image);
        }
    }

    public boolean isImageLinkedToPost(String imageUrl, Integer postId) {
        return imagemappingRepository.findByS3Url(imageUrl)
                .map(image -> image.getPost().getId().equals(postId))  // 이미지의 postId와 요청된 postId 비교
                .orElse(false);  // 이미지가 존재하지 않으면 false 반환
    }

    @Transactional
    public void updateImagePostId(List<String> imageUrls, Integer postId) {
        System.out.println("Starting updateImagePostId process...");
        System.out.println("Target Post ID: " + postId);
        System.out.println("Image URLs to update: " + imageUrls);

        // Post 객체 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    System.out.println("Post not found with ID: " + postId);
                    return new RuntimeException("해당 게시글을 찾을 수 없습니다.");
                });

        System.out.println("Post found: " + post.getTitle());

        // 이미지에 Post 설정
        for (String imageUrl : imageUrls) {
            System.out.println("Processing image with URL: " + imageUrl);

            imagemappingRepository.findByS3Url(imageUrl)
                    .ifPresentOrElse(image -> {
                        System.out.println("Image found in repository. Updating post association.");
                        image.setPost(post);  // Post 객체 설정
                        imagemappingRepository.save(image);
                        System.out.println("Image successfully updated with Post ID: " + post.getId());
                    }, () -> {
                        System.out.println("Image not found in repository for URL: " + imageUrl);
                    });
        }

        System.out.println("updateImagePostId process completed.");
//        // Post 객체 조회
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));
//
//        // 이미지에 Post 설정
//        for (String imageUrl : imageUrls) {
//            imagemappingRepository.findByS3Url(imageUrl)
//                    .ifPresent(image -> {
//                        image.setPost(post);  // Post 객체 설정
//                        imagemappingRepository.save(image);
//                    });
//        }
    }


}
