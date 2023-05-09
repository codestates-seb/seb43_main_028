package backend.section6mainproject.content.service;

import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.content.repository.WalkLogContentRepository;
import backend.section6mainproject.walklog.entity.WalkLog;
import backend.section6mainproject.walklog.service.WalkLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class WalkLogContentServiceImpl implements WalkLogContentService {
    private final WalkLogContentRepository walkLogContentRepository;
    private final StorageService storageService;
    private final WalkLogService walkLogService;

    @Override
    public WalkLogContent createWalkLogContent(WalkLogContent walkLogContent, MultipartFile contentImage) {
        walkLogService.findVerifiedWalkLog(walkLogContent.getWalkLog().getWalkLogId());
        String imageFileName = storageService.store(contentImage);
        walkLogContent.setImageFileName(imageFileName);
        return walkLogContentRepository.save(walkLogContent);
    }
}
