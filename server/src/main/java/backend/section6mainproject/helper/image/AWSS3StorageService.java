package backend.section6mainproject.helper.image;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class AWSS3StorageService implements StorageService {
    private static final float MAX_DIMENSION = 256;
    @Value("${aws-s3.access-key}")
    private String accessKey;
    @Value("${aws-s3.secret-access-key}")
    private String secretKey;
    private final String REGEX = ".*\\.([^\\.]*)";
    private final List<String> IMAGE_TYPE = List.of("jpg", "jpeg", "png");
    private S3Client s3Client;
    private S3Presigner s3Presigner;
    private Region clientRegion = Region.AP_NORTHEAST_2;
    private String bucket = "mainproject-gutter-ball-lay";
    private String thumbnailBucket = "mainproject-gutter-ball-lay-resized";

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
    public String store(MultipartFile image, String directory, boolean thumbnailOnly) {
        String imageType = getImageType(image);
        if (imageType == null) return null;

        UUID uuid = UUID.randomUUID();
        String uploadImageName = directory + "/" + uuid + "_" + image.getOriginalFilename();
        try {
            if (!thumbnailOnly) {
                uploadToS3(image.getBytes(), uploadImageName, image.getContentType(), image.getSize(), this.bucket);
            }
            ByteArrayOutputStream outputStream = resizeImageFile(image.getInputStream(), imageType);
            uploadToS3(outputStream.toByteArray(), uploadImageName, image.getContentType(), outputStream.size(), this.thumbnailBucket);

            return uploadImageName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getImageType(MultipartFile image) {
        if(image == null || image.getContentType() == null || !image.getContentType().startsWith("image")) return null;
        Matcher matcher = Pattern.compile(REGEX).matcher(image.getOriginalFilename());
        if(!matcher.matches()) return null;

        String imageType = matcher.group(1);
        if(!IMAGE_TYPE.contains(imageType)) return null;
        return imageType;
    }

    private String uploadToS3(byte[] bytes, String key, String contentType, long contentLength, String bucket) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .contentLength(contentLength)
                .build();
        try {
            this.s3Client.putObject(objectRequest, RequestBody.fromBytes(bytes));
            log.info("{} upload complete in bucket {}", objectRequest.key(), bucket);
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

    private ByteArrayOutputStream resizeImageFile(InputStream inputStream, String imageType){
        try {
            BufferedImage image = ImageIO.read(inputStream);
            inputStream.close();
            int srcHeight = image.getHeight();
            int srcWidth = image.getWidth();
            // Infer scaling factor to avoid stretching image unnaturally
            float scalingFactor = Math.min(
                    MAX_DIMENSION / srcWidth, MAX_DIMENSION / srcHeight);
            int width = (int) (scalingFactor * srcWidth);
            int height = (int) (scalingFactor * srcHeight);

            BufferedImage resizedImage = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = resizedImage.createGraphics();
            // Fill with white before applying semi-transparent (alpha) images
            graphics.setPaint(Color.white);
            graphics.fillRect(0, 0, width, height);
            // Simple bilinear resize
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.drawImage(image, 0, 0, width, height, null);
            graphics.dispose();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, imageType, outputStream);
            return outputStream;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String key) {
        if(key != null){
            try {
                DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest
                        .builder()
                        .bucket(this.bucket)
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
    }


    @Override
    public String signBucket(String key) {
        if(key != null){
            try {
                GetObjectRequest request = GetObjectRequest.builder()
                        .bucket(this.bucket)
                        .key(key)
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
        }
        return null;
    }

    @Override
    public boolean isEmptyFile(MultipartFile image) {
        if(image == null) return false;
        return image.isEmpty();
    }
}
