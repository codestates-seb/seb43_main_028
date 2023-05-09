package backend.section6mainproject.content.service;

import backend.section6mainproject.content.entity.WalkLogContent;
import org.springframework.web.multipart.MultipartFile;

public interface WalkLogContentService {
    WalkLogContent createWalkLogContent(WalkLogContent walkLogContent, MultipartFile contentImage);
}
