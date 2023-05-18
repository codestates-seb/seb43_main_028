package backend.section6mainproject.walklog.service;

import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.exception.ExceptionCode;
import backend.section6mainproject.walklog.dto.AnonymousWalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.AnonymousWalkLog;
import backend.section6mainproject.walklog.repository.AnonymousWalkLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AnonymousWalkLogServiceImpl implements AnonymousWalkLogService {
    private final AnonymousWalkLogRepository walkLogRepository;

    @Override
    public AnonymousWalkLogServiceDTO.CreateOutput createWalkLog() {
        AnonymousWalkLog anonymousWalkLog = new AnonymousWalkLog();
        anonymousWalkLog.setUserId(UUID.randomUUID().toString());
        AnonymousWalkLog savedWalkLog = walkLogRepository.save(anonymousWalkLog);
        return new AnonymousWalkLogServiceDTO.CreateOutput(savedWalkLog.getUserId());
    }

    @Override
    public AnonymousWalkLog findVerifiedWalkLogByUserId(String userId) {
        Optional<AnonymousWalkLog> optionalWalkLog = walkLogRepository.findByUserId(userId);
        return optionalWalkLog.orElseThrow(() -> new BusinessLogicException(ExceptionCode.WALK_LOG_NOT_FOUND));
    }
}
