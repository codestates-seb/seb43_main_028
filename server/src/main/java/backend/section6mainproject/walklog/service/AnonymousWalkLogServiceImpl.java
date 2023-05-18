package backend.section6mainproject.walklog.service;

import backend.section6mainproject.walklog.dto.AnonymousWalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.AnonymousWalkLog;
import backend.section6mainproject.walklog.repository.AnonymousWalkLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
