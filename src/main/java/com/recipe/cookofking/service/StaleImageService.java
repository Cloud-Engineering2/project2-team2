package com.recipe.cookofking.service;

import com.recipe.cookofking.entity.Imagemapping;
import com.recipe.cookofking.repository.ImagemappingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaleImageService {
    private final ImagemappingRepository imagemappingRepository;
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String s3BucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.cloudfront.domain}")
    private String cloudFrontDomainPrefix;

    /**
     * 매일 오전 03시
     * 해당 시각으로부터 24시간 이전에 등록되었으며
     * isTemp 상태인 이미지 검색 후
     * db와 S3에서 삭제
     */
    @Scheduled(cron = "0 0 3 * * *")  // 새벽 3시에 (03:00)에 실행
    @Transactional
    public void cleanupStaleImages() {

        log.info("Starting stale images cleanup job...");

        // 1. 특정 조건을 만족하는 데이터 조회
        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24L);

        List<Imagemapping> staleImages = imagemappingRepository.findStaleImages(twentyFourHoursAgo);

        // 1-1. 삭제할 이미지가 없다면 메소드 종료
        if (staleImages.isEmpty()) {
            log.info("No stale images were found within 24 hours");
            return;
        }

//        String prefix = "https://" + s3BucketName + ".s3." + region + ".amazonaws.com/";

        for (Imagemapping image : staleImages) {
            try {
                // 2. S3에서 파일 삭제
                String s3Key = image.getS3Url().replace(cloudFrontDomainPrefix, ""); // 예: "folder/example.png"

                DeleteObjectRequest request = DeleteObjectRequest.builder()
                        .bucket(s3BucketName)
                        .key(s3Key)
                        .build();

                s3Client.deleteObject(request);

                log.info("Deleted S3 object: {}", s3Key);

                // 3. DB에서 관련 데이터 삭제
                imagemappingRepository.deleteById(image.getId());

            } catch (Exception e) {
                log.error("Error deleting image: {}", image.getId(), e);
            }
        }

        log.info("Stale images cleanup job completed.");
    }
}
