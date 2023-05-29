package backend.section6mainproject.helper.image;

import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    /**
     * 이미지 파일을 저장하는 메서드
     *
     * @param image  이미지 파일
     * @param directory 디렉토리명
     * @param thumbnailOnly true이면 썸네일만 저장하고 false이면 썸네일과 원본 모두 저장
     * @return imageKey - 디렉토리명과 파일명이 합쳐져 있습니다. 이미지 파일이 아닌 값이 들어가면 null을 반환합니다.
     */
    String store(MultipartFile image, String directory, boolean thumbnailOnly);

    /**
     * 이미지 파일을 삭제하는 메서드
     *
     * @param key imageKey - 디렉토리명과 파일명이 합쳐져 있습니다.
     */
    void delete(String key);

    /**
     * 이미지 파일의 pre-signed url을 가져오는 메서드
     *
     * @param key imageKey - 디렉토리명과 파일명이 합쳐져 있습니다.
     * @return pre-signed url. imageKey가 null이면 null을 반환합니다.
     */
    @Named("PreSignedUrl")
    String getPreSignedUrl(String key);

    @Named("PreSignedUrlForThumbnail")
    String getPreSignedUrlForThumbnail(String key);

    boolean isEmptyFile(MultipartFile image);

}
