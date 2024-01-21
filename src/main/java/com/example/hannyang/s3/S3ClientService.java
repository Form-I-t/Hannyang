package com.example.hannyang.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Path;

@Service
public class S3ClientService {

    @Value("${cloud.aws.region.static}")
    private String region;

    public S3Client createS3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    // 파일을 S3에 업로드하는 메서드
    public void uploadFile(String bucket, String key, Path filePath) {
        S3Client s3Client = createS3Client();
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build(),
                filePath);
    }

    // 파일을 S3에서 다운로드하는 메서드
    public void downloadFile(String bucket, String key, Path downloadPath) {
        S3Client s3Client = createS3Client();
        s3Client.getObject(GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build(),
                downloadPath);
    }

    // 업로드된 파일의 URL을 생성하는 메서드
    public String getFileUrl(String bucket, String key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, key);
    }
}



