package com.playground.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfig {
    @Value("${cloud.aws.access-key}")
    private String accessKey;

    @Value("${cloud.aws.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region}")
    private String region;

    @Bean
    S3Client s3Client() {
        AwsCredentialsProvider awsCredentialsProvider = () -> AwsBasicCredentials
                .builder()
                .accessKeyId(accessKey)
                .secretAccessKey(secretKey)
                .build();

        return S3Client.builder().credentialsProvider(awsCredentialsProvider).region(Region.of(region)).build();
    }
}
