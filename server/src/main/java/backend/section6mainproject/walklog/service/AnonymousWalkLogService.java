package backend.section6mainproject.walklog.service;

import backend.section6mainproject.walklog.dto.AnonymousWalkLogServiceDTO;

public interface AnonymousWalkLogService {
    AnonymousWalkLogServiceDTO.CreateOutput createWalkLog();
}
