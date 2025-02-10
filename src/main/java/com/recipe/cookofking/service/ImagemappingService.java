package com.recipe.cookofking.service;

import com.recipe.cookofking.dto.image.ImageValidationDto;
import com.recipe.cookofking.dto.image.ImagemappingDto;
import com.recipe.cookofking.entity.Imagemapping;
import com.recipe.cookofking.mapper.ImagemappingMapper;
import com.recipe.cookofking.repository.ImagemappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImagemappingService {

    private final S3Client s3Client;
    private final ImagemappingRepository imagemappingRepository;

    // application.properties에서 버킷 이름 주입
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

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
        String s3Url = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;

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
        // 메인 이미지 검증
        imagemappingRepository.findByIdAndS3Url(validationDto.getMainImageId(), validationDto.getMainImageUrl())
                .ifPresent(image -> {
                    image.markAsPermanent();
                    imagemappingRepository.save(image);
                });

        // 조리 순서 이미지 검증
        validationDto.getStepImages().forEach(stepImage -> {
            imagemappingRepository.findByIdAndS3Url(stepImage.getImageId(), stepImage.getImageUrl())
                    .ifPresent(image -> {
                        image.markAsPermanent();
                        imagemappingRepository.save(image);
                    });
        });
    }

    @Transactional
    public void validateForUpdate(ImageValidationDto validationDto) {
        // 메인 이미지 검증
        if (validationDto.getMainImageId() != null) {
            imagemappingRepository.findByIdAndS3Url(validationDto.getMainImageId(), validationDto.getMainImageUrl())
                    .ifPresent(image -> {
                        image.markAsPermanent();
                        imagemappingRepository.save(image);
                    });
        }
        // 조리 순서 이미지 검증
        validationDto.getStepImages().forEach(stepImage -> {
            if (stepImage.getImageId() != null) {
                imagemappingRepository.findByIdAndS3Url(stepImage.getImageId(), stepImage.getImageUrl())
                        .ifPresent(image -> {
                            image.markAsPermanent();
                            imagemappingRepository.save(image);
                        });
            }
        });
    }


}
