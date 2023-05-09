package backend.section6mainproject.walklog.service;

import backend.section6mainproject.walklog.entity.WalkLog;

public interface WalkLogService {
    WalkLog createWalkLog(Long memberId);

    WalkLog updateWalkLog(WalkLog walkLog);

    WalkLog findWalkLog(Long walkLogId);
    void deleteWalkLog(Long walkLogId);
    WalkLog findVerifiedWalkLog(Long walkLogId);
    WalkLog exitWalkLog(WalkLog walkLog);

}
