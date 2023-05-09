package backend.section6mainproject.content.service;

import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    /**
     * 이미지 파일을 저장하는 메서드
     * @param image 이미지 파일
     * @return imageFileName. 이미지 파일이 아닌 값이 들어가면 null을 반환합니다.
     */
    String store(MultipartFile image);

    /**
     * 이미지 파일을 저장하는 메서드
     *
     * @param image  이미지 파일
     * @param bucket 버킷명
     * @return imageFileName. 이미지 파일이 아닌 값이 들어가면 null을 반환합니다.
     */
    String store(MultipartFile image, String bucket);

    /**
     * 이미지 파일을 삭제하는 메서드
     *
     * @param key imageFileName
     */
    void delete(String key);

    /**
     * 이미지 파일을 삭제하는 메서드
     *
     * @param key    imageFileName
     * @param bucket 버킷명
     */
    void delete(String key, String bucket);

    /**
     * 이미지 파일의 pre-signed url을 가져오는 메서드
     *
     * @param keyName imageFileName
     * @return pre-signed url
     */
    @Named("signBucket")
    String signBucket(String keyName);

    /**
     * 이미지 파일의 pre-signed url을 가져오는 메서드
     * @param keyName imageFileName
     * @param bucket 버킷명
     * @return pre-signed url
     */
    String signBucket(String keyName, String bucket);
}
