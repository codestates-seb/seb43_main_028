package backend.section6mainproject.content.service;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class AWSS3StorageService implements StorageService {
    @Value("${aws-s3.access-key}")
    private String accessKey;
    @Value("${aws-s3.secret-access-key}")
    private String secretKey;
    private S3Client s3Client;
    private S3Presigner s3Presigner;
    private Region clientRegion = Region.AP_NORTHEAST_2;
    private String bucket = "mainproject-gutter-ball-lay";

    @PostConstruct
    private void createS3Client() {
        AwsCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(awsCredentials);
        this.s3Client = S3Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(clientRegion)
                .build();

        this.s3Presigner = S3Presigner.builder()
                .credentialsProvider(credentialsProvider)
                .region(clientRegion)
                .build();
    }
    @Override
    public String store(MultipartFile image) {
        UUID uuid = UUID.randomUUID();
        String uploadImageName = uuid + "_" + image.getOriginalFilename();
        try {
            InputStream inputStream = image.getInputStream();
            String uploadedFileName = uploadToS3(inputStream, uploadImageName, image.getContentType(), image.getSize());
            return uploadedFileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String uploadToS3(InputStream is, String key, String contentType, long contentLength) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .contentLength(contentLength)
                .build();
        try {
            this.s3Client.putObject(objectRequest, RequestBody.fromInputStream(is, contentLength));
            log.info("{} upload complete", objectRequest.key());
            is.close();
            return objectRequest.key();
        } catch (AwsServiceException e) {
            log.error("# AWS S3 error", e);
        } catch (SdkClientException e) {
            log.error("# AWS S3 error", e);
        } catch (Exception e) {
            log.error("# AWS S3 error", e);
        }
        return null;
    }
    public void delete(String key) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest
                    .builder()
                    .bucket(bucket)
                    .key(key)
                    .build();
            this.s3Client.deleteObject(deleteObjectRequest);
            log.info("{}, deletion complete", key);
        } catch (AwsServiceException e) {
            log.error("# AWS S3 error", e);
        } catch (SdkClientException e) {
            log.error("# AWS S3 error", e);
        }
    }

    @Override
    public String signBucket(String keyName) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(keyName)
                    .build();
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .getObjectRequest(request)
                    .signatureDuration(Duration.ofMinutes(10))
                    .build();

            PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(presignRequest);
            URL url = presignedGetObjectRequest.url();
            log.info("pre-signed url : {}", url);
            return url.toString();

        } catch (S3Exception e) {
            log.error("# AWS S3 error", e);
        }
        return null;
    }
}
