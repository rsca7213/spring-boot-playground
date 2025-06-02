package com.playground.api.integrations.adapters;

import com.playground.api.enums.ErrorCode;
import com.playground.api.exceptions.Exception;
import com.playground.api.integrations.ports.MultimediaStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

@Service
public class AmazonS3Service implements MultimediaStorageService {
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.buckets.default}")
    private String bucketName;

    @Autowired
    public AmazonS3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String upload(MultipartFile file, String fileName) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest
                    .builder()
                    .bucket(this.bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            PutObjectResponse putObjectResponse = this.s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            if (putObjectResponse.sdkHttpResponse().isSuccessful()) {
                return String.format(
                        "https://%s.s3.%s.amazonaws.com/%s",
                        this.bucketName,
                        this.s3Client.serviceClientConfiguration().region().id(),
                        fileName
                );
            }

            throw new Exception("An error occurred while uploading the file", ErrorCode.FILE_UPLOAD_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException ioe) {
            throw new Exception("An error occurred while reading the file content", ErrorCode.FILE_READ_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new Exception("An error occurred while uploading the file", ErrorCode.FILE_UPLOAD_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
