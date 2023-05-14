package backend.section6mainproject.content.service;

import backend.section6mainproject.content.dto.WalkLogContentServiceDTO;
import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.content.mapper.WalkLogContentMapper;
import backend.section6mainproject.content.repository.WalkLogContentRepository;
import backend.section6mainproject.helper.image.StorageService;
import backend.section6mainproject.walklog.service.WalkLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WalkLogContentServiceImpl implements WalkLogContentService {
    private final WalkLogContentRepository walkLogContentRepository;
    private final StorageService storageService;
    private final WalkLogService walkLogService;
    private final WalkLogContentMapper mapper;

    @Override
    public WalkLogContentServiceDTO.CreateOutput createWalkLogContent(WalkLogContentServiceDTO.Input input) {
        WalkLogContent walkLogContent = mapper.serviceInputDTOToEntity(input);
        walkLogService.findVerifiedWalkLog(walkLogContent.getWalkLog().getWalkLogId());
        String imageKey = storageService.store(input.getContentImage(), "content");
        walkLogContent.setImageKey(imageKey);
        WalkLogContent savedWalkLogContent = walkLogContentRepository.save(walkLogContent);
        return mapper.entityToServiceCreateOutputDTO(savedWalkLogContent);
    }
}
