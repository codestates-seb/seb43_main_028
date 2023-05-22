package backend.section6mainproject.walklog.service;

import backend.section6mainproject.walklog.dto.AnonymousWalkLogServiceDTO;
import backend.section6mainproject.walklog.entity.AnonymousWalkLog;

public interface AnonymousWalkLogService {
    AnonymousWalkLogServiceDTO.CreateOutput createWalkLog();

    AnonymousWalkLogServiceDTO.Output findWalkLog(String userId);
    void exitWalkLog(String userId);
    AnonymousWalkLog findVerifiedWalkLogByUserId(String userId);
}
