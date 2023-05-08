package backend.section6mainproject.content.service;

import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(MultipartFile image);
    @Named("signBucket")
    String signBucket(String keyName);
}
