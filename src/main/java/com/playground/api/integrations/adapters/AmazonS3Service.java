package com.playground.api.integrations.adapters;

import com.playground.api.enums.ErrorCode;
import com.playground.api.exceptions.ApiException;
import com.playground.api.integrations.ports.MultimediaStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;

@Service
public class AmazonS3Service implements MultimediaStorageService {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.buckets.default}")
    private String bucketName;

    @Autowired
    public AmazonS3Service(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
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
                return fileName;
            }

            throw new ApiException("An error occurred while uploading the file", ErrorCode.FILE_UPLOAD_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException ioe) {
            throw new ApiException("An error occurred while reading the file content", ErrorCode.FILE_READ_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ApiException e) {
            throw new ApiException("An error occurred while uploading the file", ErrorCode.FILE_UPLOAD_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    public String generatePublicUrl(String uri) {
        // Generate a get object request for the specified URI
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(this.bucketName)
                .key(uri)
                .build();

        // Perform an Amazon S3 pre-signed URL generation
        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(getObjectRequest)
                .build();

        // Generate the pre-signed request
        PresignedGetObjectRequest presignedGetObjectRequest = this.s3Presigner.presignGetObject(getObjectPresignRequest);

        // Return the pre-signed URL
        return presignedGetObjectRequest.url().toExternalForm();
    }
}
