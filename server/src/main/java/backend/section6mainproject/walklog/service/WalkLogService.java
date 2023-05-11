package backend.section6mainproject.walklog.service;

import backend.section6mainproject.walklog.entity.WalkLog;

import java.util.List;

public interface WalkLogService {
    WalkLog createWalkLog(Long memberId);

    WalkLog updateWalkLog(WalkLog walkLog);

    void deleteWalkLog(Long walkLogId);
    WalkLog findWalkLog(Long walkLogId);
    List<WalkLog> findAllWalkLog();
    WalkLog findVerifiedWalkLog(Long walkLogId);
    WalkLog exitWalkLog(WalkLog walkLog);

}
