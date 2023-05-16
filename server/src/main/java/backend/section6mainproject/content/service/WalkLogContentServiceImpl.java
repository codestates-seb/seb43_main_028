package backend.section6mainproject.content.service;

import backend.section6mainproject.content.dto.WalkLogContentServiceDTO;
import backend.section6mainproject.content.entity.WalkLogContent;
import backend.section6mainproject.content.mapper.WalkLogContentMapper;
import backend.section6mainproject.content.repository.WalkLogContentRepository;
import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.exception.ExceptionCode;
import backend.section6mainproject.helper.image.StorageService;
import backend.section6mainproject.utils.CustomBeanUtils;
import backend.section6mainproject.walklog.service.WalkLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class WalkLogContentServiceImpl implements WalkLogContentService {
    private final WalkLogContentRepository walkLogContentRepository;
    private final StorageService storageService;
    private final WalkLogService walkLogService;
    private final WalkLogContentMapper mapper;

    @Override
    public WalkLogContentServiceDTO.CreateOutput createWalkLogContent(WalkLogContentServiceDTO.CreateInput createInput) {
        WalkLogContent walkLogContent = mapper.serviceCreateInputDTOToEntity(createInput);
        walkLogService.findVerifiedWalkLog(walkLogContent.getWalkLog().getWalkLogId());
        String imageKey = storageService.store(createInput.getContentImage(), "content");
        walkLogContent.setImageKey(imageKey);
        WalkLogContent savedWalkLogContent = walkLogContentRepository.save(walkLogContent);
        return mapper.entityToServiceCreateOutputDTO(savedWalkLogContent);
    }

    @Override
    public WalkLogContentServiceDTO.Output updateWalkLogContent(WalkLogContentServiceDTO.UpdateInput updateInput) {
        WalkLogContent findWalkLogContent = findVerifiedWalkLogContentInternal(updateInput.getWalkLogContentId());
        Optional.ofNullable(updateInput.getText()).ifPresent(text -> findWalkLogContent.setText(text));
        storageService.delete(findWalkLogContent.getImageKey());
        String imageKey = storageService.store(updateInput.getContentImage(), "content");
        findWalkLogContent.setImageKey(imageKey);
        return mapper.entityToServiceOutputDTO(walkLogContentRepository.save(findWalkLogContent));
    }

    @Override
    public WalkLogContentServiceDTO.Output findVerifiedWalkLogContent(Long walkLogContentId) {
        return mapper.entityToServiceOutputDTO(findVerifiedWalkLogContentInternal(walkLogContentId));
    }

    private WalkLogContent findVerifiedWalkLogContentInternal(Long walkLogContentId) {
        Optional<WalkLogContent> optionalWalkLogContent = walkLogContentRepository.findById(walkLogContentId);
        return optionalWalkLogContent.orElseThrow(() -> new BusinessLogicException(ExceptionCode.WALK_LOG_CONTENT_NOT_FOUND));
    }
}
