package backend.section6mainproject.walklog.service;

import backend.section6mainproject.walklog.dto.AnonymousWalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.AnonymousWalkLog;

public interface AnonymousWalkLogService {
    AnonymousWalkLogServiceDTO.CreateOutput createWalkLog();

    AnonymousWalkLog findVerifiedWalkLogByUserId(String userId);
}
