package com.example.hannyang.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Path;

@Service
public class S3ClientService {

    @Autowired
    private S3Client s3Client;

    @Value("${BUCKET_NAME}")
    private String bucketName;

    // 파일을 S3에 업로드하는 메서드
    public void uploadFile(String key, Path filePath) {
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(),
                filePath);
    }

    // 파일을 S3에서 다운로드하는 메서드
    public void downloadFile(String key, Path downloadPath) {
        s3Client.getObject(GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(),
                downloadPath);
    }

    // 업로드된 파일의 URL을 생성하는 메서드
    public String getFileUrl(String key) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
    }
}


