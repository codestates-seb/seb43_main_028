package backend.section6mainproject.walklog.service;

import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.exception.ExceptionCode;
import backend.section6mainproject.helper.image.StorageService;
import backend.section6mainproject.walklog.dto.AnonymousWalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.AnonymousWalkLog;
import backend.section6mainproject.walklog.mapper.AnonymousWalkLogMapper;
import backend.section6mainproject.walklog.repository.AnonymousWalkLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AnonymousWalkLogServiceImpl implements AnonymousWalkLogService {
    private final AnonymousWalkLogRepository walkLogRepository;
    private final AnonymousWalkLogMapper mapper;
    private final StorageService storageService;

    @Override
    public AnonymousWalkLogServiceDTO.CreateOutput createWalkLog() {
        AnonymousWalkLog anonymousWalkLog = new AnonymousWalkLog();
        anonymousWalkLog.setUserId(UUID.randomUUID().toString());
        AnonymousWalkLog savedWalkLog = walkLogRepository.save(anonymousWalkLog);
        return new AnonymousWalkLogServiceDTO.CreateOutput(savedWalkLog.getUserId());
    }

    @Override
    public AnonymousWalkLogServiceDTO.Output findWalkLog(String userId) {
        AnonymousWalkLog walkLog = findVerifiedWalkLogByUserId(userId);
        return mapper.entityToServiceOutputDTO(walkLog);
    }

    @Override
    public void exitWalkLog(String userId) {
        AnonymousWalkLog walkLog = findVerifiedWalkLogByUserId(userId);
        walkLog.getWalkLogContents().stream().forEach(content -> storageService.delete(content.getImageKey()));
        walkLogRepository.delete(walkLog);
    }

    @Override
    public AnonymousWalkLog findVerifiedWalkLogByUserId(String userId) {
        Optional<AnonymousWalkLog> optionalWalkLog = walkLogRepository.findByUserId(userId);
        return optionalWalkLog.orElseThrow(() -> {
            Map<String, Object> attr = new HashMap<>();
            attr.put("userId", userId);
            return new BusinessLogicException(ExceptionCode.WALK_LOG_NOT_FOUND, attr);
        });
    }
}
